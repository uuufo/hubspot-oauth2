package dev.jlarsen.hubspotoauth2.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jlarsen.hubspotoauth2.exceptions.UserExistsException;
import dev.jlarsen.hubspotoauth2.models.Hubspot.*;
import dev.jlarsen.hubspotoauth2.models.Token;
import dev.jlarsen.hubspotoauth2.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class HubspotService {

    @Autowired
    UserService userService;

    String crmApiUrl = "https://api.hubapi.com/crm/v3/objects";

    /**
     * Request objects from server based on the RequestType.
     *
     * @param requestType
     * @return
     */
    public HubspotResponse getDataFromServer(RequestType requestType, Principal principal) {
        RestTemplate restTemplate = new RestTemplate();
        String token = userService.getUser(principal.getName()).getToken().getAccess_token();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = crmApiUrl + requestType.getUrl();
        //without setting properties param the server does not return everything we want
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("properties", requestType.getProperties());
        if (requestType.name().equals("CONTACT")) {
            return restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, request, new ParameterizedTypeReference
                            <HubspotResponse<Contact>>() {
                    }).getBody();
        } else if (requestType.name().equals("COMPANY")) {
            return restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, request, new ParameterizedTypeReference
                            <HubspotResponse<Company>>() {
                    }).getBody();
        }
        return null;
    }

    /**
     * Returns a List of Contacts.
     *
     * @param principal
     * @return
     */
    public List<HubspotItemContainer<Contact>> getContacts(Principal principal) {
        HubspotResponse<Contact> response = getDataFromServer(RequestType.CONTACT, principal);
        return response.getResults();
    }

    /**
     * Returns a List of Companies.
     *
     * @param principal
     * @return
     */
    public List<HubspotItemContainer<Company>> getCompanies(Principal principal) {
        HubspotResponse<Company> response = getDataFromServer(RequestType.COMPANY, principal);
        return response.getResults();
    }

    /**
     * Receives object from web form and POSTs it to HubSpot Contact API to be saved.
     *
     * @param principal
     * @param item      to be saved
     * @return response from server
     */
    public void saveNewObject(Principal principal, HubspotItemContainer item,
                              RequestType requestType) {
        RestTemplate restTemplate = new RestTemplate();
        String token = userService.getUser(principal.getName()).getToken().getAccess_token();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HubspotItemContainer> request = new HttpEntity<>(item, headers);
        ResponseEntity<HubspotItemContainer> response = restTemplate.postForEntity(
                crmApiUrl + requestType.getUrl(), request, HubspotItemContainer.class);
    }

    /**
     * Checks if user authorization has expired.
     *
     * @param user
     * @return true if expired
     */
    public boolean isUserAuthExpired(User user) {
        LocalDateTime expires = user.getAuthStart().plusSeconds(user.getToken().getExpires_in());
        return LocalDateTime.now().isAfter(expires);
    }

    /**
     * Performs initial auth handshake or refreshes an expired authorization.
     *
     * @param code      received from server after initial request
     * @param principal
     * @return Encoded auth request URL if user has never been authorized, or null otherwise
     */
    public String authorizeUser(String code, Principal principal) {
        User user = userService.getUser(principal.getName());
        HubspotApp app = user.getApp();
        Token token = user.getToken();
        if (token == null) {
            if (code != null) {
                app.setGrant_type("authorization_code");
                app.setCode(code);
                applyTokenToUser(user, principal, exchangeCodeForToken(app));
            } else {
                return getEncodedAuthUrl(app);
            }
        } else if (isUserAuthExpired(user)) {
            applyTokenToUser(user, principal, refreshUserAuth(user, token));
        }
        return null;
    }

    /**
     * Checks if currently logged in user has ever been authorized.
     * If previous authorization has expired it is refreshed.
     *
     * @param principal
     * @return true if user is authorized
     */
    public boolean getUserAuthorization(Principal principal) {
        User user = userService.getUser(principal.getName());
        Token token = user.getToken();
        if (token == null) {
            return false;
        } else if (isUserAuthExpired(user)) {
            applyTokenToUser(user, principal, refreshUserAuth(user, token));
            return true;
        } else {
            return true;
        }
    }

    /**
     * Encodes HubspotApp object into URL parameters for initial code request.
     *
     * @param app to be encoded
     * @return Properly encoded request URL
     */
    public String getEncodedAuthUrl(HubspotApp app) {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = objectMapper.convertValue(app, Map.class);
        StringBuilder sb = new StringBuilder("https://app.hubspot.com/oauth/authorize?");
        try {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(URLEncoder.encode(params.get(key),
                        StandardCharsets.UTF_8.name()).replace("+", "%20")).append("&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Applies token to user and attempts to persist both.
     *
     * @param principal
     * @param token
     */
    public void applyTokenToUser(User user, Principal principal, Token token) {
        user.setToken(token);
        user.setAuthStart(LocalDateTime.now());
        try {
            userService.updateUser(user, principal);
        } catch (UserExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maps HubspotApp object to be used for initial token retrieval using APPLICATION_FORM_URLENCODED (not JSON)
     *
     * @param app to be mapped
     * @return Map containing object data
     */
    public MultiValueMap<String, String> mapObject(HubspotApp app) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", Collections.singletonList(app.getGrant_type()));
        map.put("client_id", Collections.singletonList(app.getClient_id()));
        map.put("client_secret", Collections.singletonList(app.getClient_secret()));
        map.put("scope", Collections.singletonList(app.getScope()));
        map.put("redirect_uri", Collections.singletonList(app.getRedirect_uri()));
        map.put("refresh_token", Collections.singletonList(app.getRefresh_token()));
        map.put("code", Collections.singletonList(app.getCode()));
        return map;
    }

    /**
     * Retrieves initial token from server by sending app data + code
     *
     * @param app to retrieve token for
     * @return token received
     */
    public Token exchangeCodeForToken(HubspotApp app) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = mapObject(app);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Token> response = restTemplate.postForEntity("https://api.hubapi.com/oauth/v1/token",
                request, Token.class);
        return response.getBody();
    }

    /**
     * Retrieves new auth token from server by sending app data including refresh_token
     *
     * @param user to receive new token
     * @return token received
     */
    public Token refreshUserAuth(User user, Token oldToken) {
        HubspotApp app = user.getApp();
        app.setGrant_type("refresh_token");
        app.setRefresh_token(user.getToken().getRefresh_token());
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = mapObject(app);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Token> response = restTemplate.postForEntity("https://api.hubapi.com/oauth/v1/token",
                request, Token.class);
        Token newToken = response.getBody();
        //apply new refresh_token to token pulled from database to avoid attempt to save detached entity
        if (newToken != null) {
            oldToken.setAccess_token(newToken.getAccess_token());
            oldToken.setExpires_in(newToken.getExpires_in());
        }
        return oldToken;
    }
}

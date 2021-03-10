package dev.jlarsen.hubspotoauth2.models.Hubspot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HubspotApp {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "app_generator")
    private Long Id;

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
    private String redirect_uri;
    private String refresh_token;
    private String code;

}

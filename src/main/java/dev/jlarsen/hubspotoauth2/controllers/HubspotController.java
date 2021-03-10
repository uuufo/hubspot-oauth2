package dev.jlarsen.hubspotoauth2.controllers;

import dev.jlarsen.hubspotoauth2.models.Hubspot.Company;
import dev.jlarsen.hubspotoauth2.models.Hubspot.Contact;
import dev.jlarsen.hubspotoauth2.models.Hubspot.HubspotItemContainer;
import dev.jlarsen.hubspotoauth2.models.Hubspot.RequestType;
import dev.jlarsen.hubspotoauth2.services.HubspotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HubspotController {

    @Autowired
    HubspotService hubspotService;

    /**
     * Retrieve list of contacts and attach empty "contact" to model for form use.
     */
    @GetMapping(value = "/hubspot/contacts")
    public String getContacts(Principal principal, Model model) {
        if (!hubspotService.getUserAuthorization(principal)) {
            return "redirect:/hubspot";
        }
        model.addAttribute("contacts", hubspotService.getContacts(principal));
        model.addAttribute("contact", new Contact());
        return "/hubspot/contacts";
    }

    /**
     * Submit new "contact" from web form to server.
     */
    @PostMapping(value = "/hubspot/contacts")
    public String saveContact(@Valid @ModelAttribute("contact") Contact contact,
                              Principal principal, WebRequest request) {
        if (!hubspotService.getUserAuthorization(principal)) {
            return "redirect:/hubspot";
        }
        hubspotService.saveNewObject(principal, new HubspotItemContainer<>(contact), RequestType.CONTACT);
        return "redirect:/hubspot/contacts";
    }

    /**
     * Retrieve list of companies and attach empty "company" to model for form use.
     */
    @GetMapping(value = "/hubspot/companies")
    public String getCompanies(Principal principal, Model model) {
        if (!hubspotService.getUserAuthorization(principal)) {
            return "redirect:/hubspot";
        }
        model.addAttribute("companies", hubspotService.getCompanies(principal));
        model.addAttribute("company", new Company());
        return "/hubspot/companies";
    }

    /**
     * Submit new "company" from web form to server.
     */
    @PostMapping(value = "/hubspot/companies")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              Principal principal) {
        if (!hubspotService.getUserAuthorization(principal)) {
            return "redirect:/hubspot";
        }
        hubspotService.saveNewObject(principal, new HubspotItemContainer<>(company), RequestType.COMPANY);
        return "redirect:/hubspot/companies";
    }


    /**
     * This page serves as redirect_uri for initial user authorization.
     *
     * @param code received from server after initial request
     */
    @GetMapping("/hubspot")
    public String hubspotHub(@RequestParam(required = false) String code, Principal principal) {
        String redirect = hubspotService.authorizeUser(code, principal);
        if (redirect == null) {
            return "hubspot";
        } else {
            return "redirect:" + redirect;
        }
    }
}

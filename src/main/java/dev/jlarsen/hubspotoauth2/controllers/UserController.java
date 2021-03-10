package dev.jlarsen.hubspotoauth2.controllers;

import dev.jlarsen.hubspotoauth2.exceptions.UserExistsException;
import dev.jlarsen.hubspotoauth2.facades.UserFacade;
import dev.jlarsen.hubspotoauth2.models.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    UserFacade userFacade;

    @GetMapping("/login")
    public String loginUser() {
        return "login";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userFacade.getUserList());
        return "users";
    }

    @GetMapping("/denied")
    public String accessDenied() {
        return "denied";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        userFacade.populateModel(model);
        return "register_form";
    }

    @PostMapping("/register")
    public String submitRegisterForm(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            userFacade.populateModel(model);
            return "register_form";
        } else {
            try {
                userFacade.registerNewUserAccount(user);
            } catch (UserExistsException e) {
                bindingResult.rejectValue("email", "user.email", e.getMessage());
                userFacade.populateModel(model);
                return "register_form";
            }
            return "register_success";
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        userFacade.populateProfile(model, principal);
        return "profile";
    }

    @PostMapping("/profile")
    public String submitProfileForm(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult,
                                    Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            userFacade.populateProfile(model, principal);
            return "profile";
        } else {
            try {
                userFacade.updateUserAccountProfile(user, principal);
            } catch (UserExistsException e) {
                userFacade.populateProfile(model, principal);
                bindingResult.rejectValue("email", "user.email", e.getMessage());
                return "profile";
            }
            return "profile_success";
        }
    }
}
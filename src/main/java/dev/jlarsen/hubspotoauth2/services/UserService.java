package dev.jlarsen.hubspotoauth2.services;

import dev.jlarsen.hubspotoauth2.exceptions.UserExistsException;
import dev.jlarsen.hubspotoauth2.models.Hubspot.HubspotApp;
import dev.jlarsen.hubspotoauth2.models.Role;
import dev.jlarsen.hubspotoauth2.models.User;
import dev.jlarsen.hubspotoauth2.repositories.HubspotAppRepository;
import dev.jlarsen.hubspotoauth2.repositories.RoleRepository;
import dev.jlarsen.hubspotoauth2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    HubspotAppRepository hubspotAppRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User persistUser(User user) throws UserExistsException {
        if (emailExists(user.getEmail())) {
            throw new UserExistsException("An account already exists for this email: " + user.getEmail());
        }
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole;
        if (user.isHuman()) {
            userRole = roleRepository.findByRole("ADMIN");
        } else {
            userRole = roleRepository.findByRole("USER");
        }
        Optional<HubspotApp> app = hubspotAppRepository.findById(1L);
        user.setApp(app.get());
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user, Principal principal) throws UserExistsException {
        User updatedUser = userRepository.findByEmail(principal.getName());
        if (!user.getEmail().equals(updatedUser.getEmail())) {
            if (emailExists(user.getEmail())) {
                throw new UserExistsException("An account already exists for this email: " + user.getEmail());
            }
        }
        //BeanUtils.copyProperties(user, updatedUser);
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setBirthday((user.getBirthday()));
        updatedUser.setMood(user.getMood());
        updatedUser.setProfession(user.getProfession());
        updatedUser.setToken((user.getToken()));
        return userRepository.save(updatedUser);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}

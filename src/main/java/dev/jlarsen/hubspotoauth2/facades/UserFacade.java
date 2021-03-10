package dev.jlarsen.hubspotoauth2.facades;

import dev.jlarsen.hubspotoauth2.exceptions.UserExistsException;
import dev.jlarsen.hubspotoauth2.models.User;
import dev.jlarsen.hubspotoauth2.models.UserDto;
import dev.jlarsen.hubspotoauth2.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.*;

@Component
public class UserFacade {

    @Autowired
    UserService userService;

    @Transactional
    public User registerNewUserAccount(UserDto userDto) throws UserExistsException {
        User user = convertDtoToUser(userDto);
        return userService.persistUser(user);
    }

    @Transactional
    public User updateUserAccountProfile(UserDto userDto, Principal principal) throws UserExistsException {
        User user = convertDtoToUser(userDto);
        return userService.updateUser(user, principal);
    }

    public List<UserDto> getUserList() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userService.getUsers()) {
            users.add(convertUserToDto(user));
        }
        return users;
    }

    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setBirthday(user.getBirthday());
        userDto.setMood(user.getMood());
        userDto.setProfession(user.getProfession());
        userDto.setHuman(user.isHuman());
        userDto.setEnabled(user.isEnabled());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    public User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setProfession(userDto.getProfession());
        user.setBirthday(userDto.getBirthday());
        user.setMood(userDto.getMood());
        user.setHuman(userDto.isHuman());
        return user;
    }

    public void populateModel(Model model) {
        if (!model.containsAttribute("user")) {
            User user = new User();
            model.addAttribute("user", user);
        }
        populateProfessionList(model);
    }

    public void populateProfile(Model model, Principal principal) {
        UserDto currentUser = convertUserToDto(userService.getUser(principal.getName()));
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", currentUser);
        } else {
            BeanUtils.copyProperties(currentUser, model.getAttribute("user"));
        }
        populateProfessionList(model);
    }

    public void populateProfessionList(Model model) {
        List<String> listProfession = Arrays.asList("Developer", "Tester", "Architect");
        model.addAttribute("listProfession", listProfession);
    }
}

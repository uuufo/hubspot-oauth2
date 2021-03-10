package dev.jlarsen.hubspotoauth2.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserDto {

    @Size(min=5, max=40, message="Please enter First and Last name.")
    private String name;

    @NotBlank(message = "Please enter a valid e-mail address.")
    @Email(message = "Please enter a valid e-mail address.")
    private String email;

    @Size(min = 8, max = 25, message="Must be between 8 and 25 characters.")
    private String password;

    @NotBlank(message="Please choose a Mood.")
    private String mood;

    private String birthday;

    private boolean human;

    private boolean enabled;

    @NotBlank(message = "Please choose a profession.")
    private String profession;

    private Set<Role> roles;

    public UserDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mood='" + mood + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }
}

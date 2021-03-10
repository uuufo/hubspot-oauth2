package dev.jlarsen.hubspotoauth2.models.Hubspot;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {

    private String name;
    private String city;
    private String state;
    private String phone;
    private String domain;
}

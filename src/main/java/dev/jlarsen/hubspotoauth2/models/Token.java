package dev.jlarsen.hubspotoauth2.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Token {

    @Id
    private Long user_id;

    private String refresh_token;
    private String access_token;
    private Long expires_in;

    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}

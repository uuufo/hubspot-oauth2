package dev.jlarsen.hubspotoauth2.repositories;

import dev.jlarsen.hubspotoauth2.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}

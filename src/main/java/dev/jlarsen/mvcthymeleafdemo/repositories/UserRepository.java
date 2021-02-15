package dev.jlarsen.mvcthymeleafdemo.repositories;

import dev.jlarsen.mvcthymeleafdemo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}

package dev.jlarsen.mvcthymeleafdemo.repositories;

import dev.jlarsen.mvcthymeleafdemo.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByRole(String role);
}

package dev.jlarsen.hubspotoauth2.repositories;

import dev.jlarsen.hubspotoauth2.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByRole(String role);
}

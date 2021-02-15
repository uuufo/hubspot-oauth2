package dev.jlarsen.mvcthymeleafdemo;

import dev.jlarsen.mvcthymeleafdemo.models.Role;
import dev.jlarsen.mvcthymeleafdemo.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class MvcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadInitialRoleData(RoleRepository roleRepository) {
		return (args) -> {
			if (((Collection<Role>)roleRepository.findAll()).size() == 0) {
				roleRepository.save(new Role("ADMIN"));
				roleRepository.save(new Role("USER"));
			}
		};
	}


}

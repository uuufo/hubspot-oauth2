package dev.jlarsen.hubspotoauth2;

import dev.jlarsen.hubspotoauth2.models.Hubspot.HubspotApp;
import dev.jlarsen.hubspotoauth2.models.Role;
import dev.jlarsen.hubspotoauth2.repositories.HubspotAppRepository;
import dev.jlarsen.hubspotoauth2.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadInitialData(RoleRepository roleRepository, HubspotAppRepository hubspotAppRepository) {
		return (args) -> {
			if (((Collection<Role>)roleRepository.findAll()).size() == 0) {
				roleRepository.save(new Role("ADMIN"));
				roleRepository.save(new Role("USER"));
			}

			if (((Collection<HubspotApp>)hubspotAppRepository.findAll()).size() == 0) {
				HubspotApp testApp1 = new HubspotApp();
				testApp1.setClient_id(" test client id ");
				testApp1.setClient_secret(" test client secret");
				testApp1.setScope("contacts social");
				testApp1.setRedirect_uri("http://localhost:8080/hubspot");
				hubspotAppRepository.save(testApp1);
			}
		};
	}
}

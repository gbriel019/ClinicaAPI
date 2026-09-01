package com.clinica.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		Map<String, Object> properties = Map.of(
				"spring.datasource.url", dotenv.get("DB_URL"),
				"spring.datasource.username", dotenv.get("DB_USERNAME"),
				"spring.datasource.password", dotenv.get("DB_PASSWORD"),
				"api.security.token.secret", dotenv.get("JWT_SECRET")
		);

		SpringApplication app = new SpringApplication(ApiApplication.class);

		app.setDefaultProperties(properties);

		app.run(args);
	}
}
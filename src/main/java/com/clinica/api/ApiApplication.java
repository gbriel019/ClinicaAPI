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
				"DB_URL", dotenv.get("DB_URL"),
				"DB_USERNAME", dotenv.get("DB_USERNAME"),
				"DB_PASSWORD", dotenv.get("DB_PASSWORD"),
				"JWT_SECRET", dotenv.get("JWT_SECRET")
		);

		SpringApplication app = new SpringApplication(ApiApplication.class);

		app.setDefaultProperties(properties);

		app.run(args);
	}
}
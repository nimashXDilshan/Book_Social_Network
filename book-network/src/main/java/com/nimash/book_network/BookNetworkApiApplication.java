package com.nimash.book_network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
//import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetworkApiApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.configure().load();
//		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
//
//		System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
//		System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
//		System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
//		System.setProperty("application.security.jwt.secret-Key", dotenv.get("JWT_SECRET"));

		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

}

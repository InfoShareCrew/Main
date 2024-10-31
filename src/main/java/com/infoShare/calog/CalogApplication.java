package com.infoShare.calog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalogApplication.class, args);
	}

}

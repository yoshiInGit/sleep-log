package com.yoshi.sleep_log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SleepLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleepLogApplication.class, args);
	}

}

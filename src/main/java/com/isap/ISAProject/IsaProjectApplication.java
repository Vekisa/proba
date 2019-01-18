package com.isap.ISAProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
@EnableTransactionManagement
public class IsaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsaProjectApplication.class, args);
	}
}

package com.money.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;





@SpringBootApplication

@ComponentScan({"com.money.api.resource"})
@EntityScan("com.money.api.model")
@EnableJpaRepositories("com.money.api.repository")
public class MoneyApiApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(MoneyApiApplication.class, args);
	}
}

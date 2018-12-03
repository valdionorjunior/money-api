package com.money.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;





@SpringBootApplication

public class MoneyApiApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(MoneyApiApplication.class, args);
	}
	
	/*
	//Configuração GLOBAL DO CROSSORIGIN
	// Habilitando o crossorigin para todas as requisiçoes da minha aplicação
	// esse Bean previne o erro de requisição cruzada - ou seja requisição vinda de outro servido, 
	// o Angular por exemplo usa um servidor diferente para acessa a api.
	// erro que previne ->  Access-Control-Allow-Origin: ... 
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("http://localhost:4200");
            }
        };
    }*/
    
}

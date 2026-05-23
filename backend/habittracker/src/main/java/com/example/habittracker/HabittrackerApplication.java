package com.example.habittracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.habittracker.config.CorsProperties;
import com.example.habittracker.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, CorsProperties.class})
public class HabittrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabittrackerApplication.class, args);
	}

}

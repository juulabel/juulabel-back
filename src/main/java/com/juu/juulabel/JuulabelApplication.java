package com.juu.juulabel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class JuulabelApplication {

	public static void main(String[] args) {
		SpringApplication.run(JuulabelApplication.class, args);
	}

}

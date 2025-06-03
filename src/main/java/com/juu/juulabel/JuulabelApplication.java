package com.juu.juulabel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
@EnableConfigurationProperties
public class JuulabelApplication {

	public static void main(String[] args) {
		SpringApplication.run(JuulabelApplication.class, args);
	}

}

package com.juu.juulabel.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// LocalDate, LocalDateTime 등을 JSON으로 직렬화/역직렬화
		objectMapper.registerModule(new JavaTimeModule());
		// 날짜와 시간을 타임스탬프(숫자) 형식으로 쓰지 않도록 설정, ISO-8601 문자열 형식으로 날짜를 직렬화(2024-07-07T14:30:00)
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// JSON 필드 이름을 스네이크 케이스(snake_case)로 변환 설정(accessToken 필드는 access_token으로 변환)
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		return objectMapper;
	}
}

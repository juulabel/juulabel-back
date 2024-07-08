package com.juu.juulabel.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.juu.juulabel.api"})
public class FeignClientConfig {
}

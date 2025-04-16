package com.juu.juulabel.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.juu.juulabel"})
public class FeignClientConfig {
}

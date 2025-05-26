package com.juu.juulabel.common.config;

import com.juu.juulabel.common.converter.ProviderConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final ProviderConverter providerConverter;
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(providerConverter);
    }
} 
package com.juu.juulabel;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class EbHealthCheck {
    @GetMapping("/")
    public String home() {
        return "<h1>Juulabel<h1>";
    }
}

package com.juu.juulabel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbHealthCheck {
    @GetMapping("/")
    public String home() {
        return "<h1>Juulabel<h1>";
    }
}

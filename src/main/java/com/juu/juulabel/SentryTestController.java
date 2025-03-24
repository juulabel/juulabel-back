package com.juu.juulabel;


import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentry")
public class SentryTestController {

    @GetMapping
    public void test() {
        try{
            throw new RuntimeException("222");
        }catch (Exception e){
            Sentry.captureException(e);
        }
    }
}

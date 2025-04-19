package com.juu.juulabel.common.client;

import com.juu.juulabel.member.token.GoogleToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "google-auth", url = "${api.google.gauth}")
public interface GoogleAuthClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleToken generateOAuthToken(@RequestParam(name = "code") String code,
                                   @RequestParam(name = "client_id") String clientId,
                                   @RequestParam(name = "client_secret") String clientSecret,
                                   @RequestParam(name = "redirect_uri") String redirectUri,
                                   @RequestParam(name = "grant_type") String grantType);

}
package com.juu.juulabel.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.juu.juulabel.member.request.ApplePublicKey;
import com.juu.juulabel.member.token.AppleToken;

@FeignClient(value = "apple-auth", url = "${api.apple.aauth}")
public interface AppleAuthClient {

    @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleToken generateOAuthToken(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "client_secret") String clientSecret,
            @RequestParam(name = "redirect_uri") String redirectUri,
            @RequestParam(name = "grant_type") String grantType);

    @GetMapping("/auth/keys")
    List<ApplePublicKey> getApplePublicKeys();
}

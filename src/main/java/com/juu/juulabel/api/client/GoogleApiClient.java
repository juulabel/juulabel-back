package com.juu.juulabel.api.client;

import com.juu.juulabel.domain.dto.member.GoogleUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "google-api", url = "${api.google.gapi}")
public interface GoogleApiClient {

    @GetMapping("/userinfo/v2/me")
    GoogleUser getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

}
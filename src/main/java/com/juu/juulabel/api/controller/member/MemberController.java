package com.juu.juulabel.api.controller.member;

import com.juu.juulabel.api.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/members"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

}

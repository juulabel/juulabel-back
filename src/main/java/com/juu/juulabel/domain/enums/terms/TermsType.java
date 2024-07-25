package com.juu.juulabel.domain.enums.terms;

import com.juu.juulabel.domain.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType implements Code {

    SERVICE("TR000", "서비스 이용약관"),
    PRIVACY("TR001", "개인정보 수집 및 이용동의"),
    MARKETING("TR002", "마케팅 수신")
    ;

    private final String code;
    private final String description;
}

package com.juu.juulabel.member.token;

import java.util.Date;

public record Token(
        String accessToken,
        Date accessExpiredAt
) {
}

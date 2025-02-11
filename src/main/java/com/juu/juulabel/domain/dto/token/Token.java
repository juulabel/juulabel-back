package com.juu.juulabel.domain.dto.token;

import java.util.Date;

public record Token(
        String accessToken,
        Date accessExpiredAt
) {
}

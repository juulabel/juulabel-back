package com.juu.juulabel.domain.embedded;

import com.juu.juulabel.domain.converter.FlavorLevelConverter;
import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Flavor {

    @Convert(converter = FlavorLevelConverter.class)
    @Column(name = "sweetness", columnDefinition = "int comment '단맛'")
    private FlavorLevel sweetness;

    @Convert(converter = FlavorLevelConverter.class)
    @Column(name = "sourness", columnDefinition = "int comment '신맛'")
    private FlavorLevel sourness;

    @Convert(converter = FlavorLevelConverter.class)
    @Column(name = "bitterness", columnDefinition = "int comment '쓴맛'")
    private FlavorLevel bitterness;

    @Convert(converter = FlavorLevelConverter.class)
    @Column(name = "umami", columnDefinition = "int comment '감칠맛'")
    private FlavorLevel umami;

    @Convert(converter = FlavorLevelConverter.class)
    @Column(name = "aftertaste", columnDefinition = "int comment '여운'")
    private FlavorLevel aftertaste;

    @Convert(converter = FlavorLevelConverter.class)
    @Column(name = "body", columnDefinition = "int comment '무게감'")
    private FlavorLevel body;

}

package com.juu.juulabel.domain.embedded;

import com.juu.juulabel.domain.converter.*;
import com.juu.juulabel.domain.enums.alcohol.sensory.*;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Sensory {

    /**
     * 탁주, 약청주
     */
    @Convert(converter = TurbidityLevelConverter.class)
    @Column(name = "turbidity", columnDefinition = "int comment '탁도'")
    private TurbidityLevel turbidity;

    /**
     * 탁주, 과실주, 기타
     */
    @Convert(converter = CarbonationLevelConverter.class)
    @Column(name = "carbonation", columnDefinition = "int comment '탄산도'")
    private CarbonationLevel carbonation;

    /**
     * 탁주, 소주/증류주, 약청주
     */
    @Convert(converter = ViscosityLevelConverter.class)
    @Column(name = "viscosity", columnDefinition = "int comment '점성도'")
    private ViscosityLevel viscosity;

    /**
     * 소주
     */
    @Convert(converter = ClarityLevelConverter.class)
    @Column(name = "clarity", columnDefinition = "int comment '투명도'")
    private ClarityLevel clarity;

    /**
     * 약청주, 기타
     */
    @Convert(converter = SedimentLevelConverter.class)
    @Column(name = "sediment", columnDefinition = "int comment '침전물'")
    private SedimentLevel sediment;

    /**
     * 과실주, 기타
     */
    @Convert(converter = DensityLevelConverter.class)
    @Column(name = "density", columnDefinition = "int comment '진하기'")
    private DensityLevel density;

}

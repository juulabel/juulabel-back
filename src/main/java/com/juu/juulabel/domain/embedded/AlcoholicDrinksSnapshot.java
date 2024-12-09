package com.juu.juulabel.domain.embedded;

import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetails;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.Brewery;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlcoholicDrinksSnapshot {

    @Column(name = "alcohol_type_name", columnDefinition = "varchar(100) comment '전통주 주종 이름'")
    private String alcoholTypeName;

    @Column(name = "alcoholic_drinks_name", columnDefinition = "varchar(100) comment '전통주 이름'")
    private String alcoholicDrinksName;

    @Column(name = "alcohol_content", columnDefinition = "DECIMAL(4,2) comment '알코올 도수'")
    private Double alcoholContent;

    @Column(name = "brewery_name", columnDefinition = "varchar(100) comment '양조장 이름'")
    private String breweryName;

    @Column(name = "brewery_region", columnDefinition = "varchar(100) comment '양조장 지역'")
    private String breweryRegion;

    public static AlcoholicDrinksSnapshot of(AlcoholType alcoholType,
                                             AlcoholicDrinks alcoholicDrinks,
                                             Brewery brewery) {
        return AlcoholicDrinksSnapshot.builder()
            .alcoholTypeName(alcoholType.getName())
            .alcoholicDrinksName(alcoholicDrinks.getName())
            .alcoholContent(alcoholicDrinks.getAlcoholContent())
            .breweryName(brewery.getName())
            .build();
    }

    public static AlcoholicDrinksSnapshot unofficial(String alcoholTypeName,
                                                     String alcoholicDrinksName,
                                                     Double alcoholContent,
                                                     String breweryName) {
        return AlcoholicDrinksSnapshot.builder()
            .alcoholTypeName(alcoholTypeName)
            .alcoholicDrinksName(alcoholicDrinksName)
            .alcoholContent(alcoholContent)
            .breweryName(breweryName)
            .build();
    }

    public static AlcoholicDrinksSnapshot fromDto(AlcoholicDrinksDetails alcoholicDrinks) {
        return AlcoholicDrinksSnapshot.builder()
            .alcoholTypeName(alcoholicDrinks.alcoholTypeName())
            .alcoholicDrinksName(alcoholicDrinks.alcoholicDrinksName())
            .alcoholContent(alcoholicDrinks.alcoholContent())
            .breweryName(alcoholicDrinks.breweryName())
            .breweryRegion(alcoholicDrinks.breweryRegion())
            .build();
    }


}

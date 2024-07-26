package com.juu.juulabel.domain.embedded;

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
public class AlcoholSnapshot {

    @Column(name = "alcoholic_drinks_name", columnDefinition = "varchar(100) comment '전통주 이름'")
    private String alcoholicDrinksName;

    @Column(name = "alcohol_content", columnDefinition = "DECIMAL(4,2) comment '알코올 도수'")
    private Double alcoholContent;

    @Column(name = "brewery_name", columnDefinition = "varchar(100) comment '양조장 이름'")
    private String breweryName;

    public static AlcoholSnapshot official(AlcoholicDrinks alcoholicDrinks,
                                           Brewery brewery) {
        return AlcoholSnapshot.builder()
                .alcoholicDrinksName(alcoholicDrinks.getName())
                .alcoholContent(alcoholicDrinks.getAlcoholContent())
                .breweryName(brewery.getName())
                .build();
    }

    public static AlcoholSnapshot unofficial(String alcoholicDrinksName,
                                             Double alcoholContent,
                                             String breweryName) {
        return AlcoholSnapshot.builder()
                .alcoholicDrinksName(alcoholicDrinksName)
                .alcoholContent(alcoholContent)
                .breweryName(breweryName)
                .build();
    }

}

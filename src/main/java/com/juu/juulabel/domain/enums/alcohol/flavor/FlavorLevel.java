package com.juu.juulabel.domain.enums.alcohol.flavor;

import com.juu.juulabel.domain.enums.Rateable;

public enum FlavorLevel implements Rateable {
    VERY_LOW(1, "매우 낮음"),
    LOW(2, "낮음"),
    MEDIUM(3, "중간"),
    HIGH(4, "높음"),
    VERY_HIGH(5, "매우 높음");

    private final int score;
    private final String description;

    FlavorLevel(int score, String description) {
        this.score = score;
        this.description = description;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public String getDescription() {
        return description;
    }

}

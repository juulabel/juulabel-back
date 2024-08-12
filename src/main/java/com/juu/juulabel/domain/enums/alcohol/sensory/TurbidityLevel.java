package com.juu.juulabel.domain.enums.alcohol.sensory;

import com.juu.juulabel.domain.enums.Rateable;

public enum TurbidityLevel implements Rateable {
    CLEAR(1, "맑음"),
    SOMEWHAT_CLEAR(2, "맑은 편"),
    MEDIUM(3, "중간"),
    SOMEWHAT_CLOUDY(4, "탁한 편"),
    CLOUDY(5, "탁함");

    private final int score;
    private final String description;

    TurbidityLevel(int score, String description) {
        this.score = score;
        this.description = description;
    }

    @Override
    public String getName() {
        return name();
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

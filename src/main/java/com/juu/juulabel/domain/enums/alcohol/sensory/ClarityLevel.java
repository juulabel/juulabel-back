package com.juu.juulabel.domain.enums.alcohol.sensory;

import com.juu.juulabel.domain.enums.Rateable;

public enum ClarityLevel implements Rateable {
    CLEAR(1, "맑음"),
    SOMEWHAT_CLEAR(2, "맑은 편"),
    MEDIUM(3, "중간"),
    SOMEWHAT_CLOUDY(4, "흐린 편"),
    CLOUDY(5, "흐림");

    private final int score;
    private final String description;

    ClarityLevel(int score, String description) {
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
package com.juu.juulabel.domain.enums.alcohol.sensory;

import com.juu.juulabel.domain.enums.Rateable;

public enum ViscosityLevel implements Rateable  {
    NONE(1, "없음"),
    LOW(2, "적음"),
    MEDIUM(3, "중간"),
    SOMEWHAT_HIGH(4, "있는 편"),
    HIGH(5, "많음");

    private final int score;
    private final String description;

    ViscosityLevel(int score, String description) {
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


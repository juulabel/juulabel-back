package com.juu.juulabel.domain.enums.alcohol.sensory;

import com.juu.juulabel.domain.enums.Rateable;

public enum DensityLevel implements Rateable {
    LIGHT(1, "연함"),
    SOMEWHAT_LIGHT(2, "연한 편"),
    MEDIUM(3, "중간"),
    SOMEWHAT_DARK(4, "진한 편"),
    DARK(5, "진함");

    private final int score;
    private final String description;

    DensityLevel(int score, String description) {
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

package com.juu.juulabel.domain.enums.alcohol.sensory;

import com.juu.juulabel.domain.enums.Rateable;

public enum SedimentLevel implements Rateable {
    NONE(1, "없음"),
    SOME(5, "있음");

    private final int score;
    private final String description;

    SedimentLevel(int score, String description) {
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

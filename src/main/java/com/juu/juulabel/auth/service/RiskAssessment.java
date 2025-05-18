package com.juu.juulabel.auth.service;

import lombok.Getter;

/**
 * Risk assessment result
 */
@Getter
public class RiskAssessment {
    private final double score; // 0.0 (low) to 1.0 (high)
    private final String reason;
    private final boolean familyShouldBeCompromised;

    public RiskAssessment(double score, String reason, boolean familyShouldBeCompromised) {
        this.score = score;
        this.reason = reason;
        this.familyShouldBeCompromised = familyShouldBeCompromised;
    }

    public boolean isHighRisk() {
        /* e.g., score > 0.8 */
        return score > 0.8;
    }

    public boolean isFamilyCompromised() {
        return familyShouldBeCompromised;
    }
}

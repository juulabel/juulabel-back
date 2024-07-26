package com.juu.juulabel.domain.enums;

/**
 * description : interface used to grade properties (e.g. carbonation, viscosity, etc.)
 */
public interface Rateable {
    Integer getScore();
    String getDescription();
}

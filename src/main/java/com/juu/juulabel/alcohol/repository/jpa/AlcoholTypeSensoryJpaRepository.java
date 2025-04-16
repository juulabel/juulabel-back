package com.juu.juulabel.alcohol.repository.jpa;

import com.juu.juulabel.alcohol.domain.AlcoholTypeSensory;
import com.juu.juulabel.alcohol.domain.SensoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlcoholTypeSensoryJpaRepository extends JpaRepository<AlcoholTypeSensory, Long> {

    @Query("SELECT sl " +
            "FROM SensoryLevel sl " +
            "JOIN Sensory s ON sl.sensory.id = s.id " +
            "JOIN AlcoholTypeSensory ats ON ats.sensory.id = s.id " +
            "WHERE ats.alcoholType.id = :alcoholTypeId AND ats.isUsed = true " +
            "ORDER BY sl.id ASC")
    List<SensoryLevel> findAllSensoryLevelByAlcoholTypeId(@Param("alcoholTypeId") Long alcoholTypeId);
}

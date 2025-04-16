package com.juu.juulabel.alcohol.repository.jpa;

import com.juu.juulabel.alcohol.domain.AlcoholTypeFlavor;
import com.juu.juulabel.alcohol.domain.FlavorLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlcoholTypeFlavorJpaRepository extends JpaRepository<AlcoholTypeFlavor, Long> {

    @Query("SELECT fl " +
            "FROM FlavorLevel fl " +
            "JOIN Flavor f ON fl.flavor.id = f.id " +
            "JOIN AlcoholTypeFlavor atf ON atf.flavor.id = f.id " +
            "WHERE atf.alcoholType.id = :alcoholTypeId AND atf.isUsed = true " +
            "ORDER BY fl.id ASC")
    List<FlavorLevel> findAllSensoryLevelByAlcoholTypeId(@Param("alcoholTypeId") Long alcoholTypeId);
}

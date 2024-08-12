package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.AlcoholTypeColor;
import com.juu.juulabel.domain.entity.alcohol.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlcoholTypeColorJpaRepository extends JpaRepository<AlcoholTypeColor, Long> {

    @Query("SELECT c FROM Color c JOIN AlcoholTypeColor atc ON atc.color.id = c.id WHERE atc.alcoholType.id = :alcoholTypeId AND atc.isUsed = true ORDER BY c.id ASC")
    List<Color> findAllByAlcoholTypeId(@Param("alcoholTypeId") Long alcoholTypeId);
}

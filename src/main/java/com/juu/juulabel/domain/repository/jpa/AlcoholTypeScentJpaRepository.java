package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.AlcoholTypeScent;
import com.juu.juulabel.domain.entity.alcohol.Scent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlcoholTypeScentJpaRepository extends JpaRepository<AlcoholTypeScent, Long> {

    @Query("SELECT s FROM Scent s JOIN AlcoholTypeScent ats ON ats.scent.id = s.id WHERE ats.alcoholType.id = :alcoholTypeId AND ats.isUsed = true ORDER BY s.id ASC")
    List<Scent> findAllByAlcoholTypeId(@Param("alcoholTypeId") Long alcoholTypeId);
}

package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.Scent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScentJpaRepository extends JpaRepository<Scent, Long> {

    List<Scent> findByIdIn(List<Long> ids);

}

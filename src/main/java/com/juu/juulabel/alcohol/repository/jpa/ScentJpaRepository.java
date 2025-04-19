package com.juu.juulabel.alcohol.repository.jpa;

import com.juu.juulabel.alcohol.domain.Scent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScentJpaRepository extends JpaRepository<Scent, Long> {

    List<Scent> findByIdIn(List<Long> ids);

}

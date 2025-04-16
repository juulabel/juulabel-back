package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeCommentJpaRepository extends JpaRepository<DailyLifeComment, Long> {
    Optional<DailyLifeComment> findByIdAndDeletedAtIsNull(Long id);
}
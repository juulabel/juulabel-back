package com.juu.juulabel.dailylife.repository.jpa;

import com.juu.juulabel.dailylife.domain.DailyLifeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeCommentJpaRepository extends JpaRepository<DailyLifeComment, Long> {
    Optional<DailyLifeComment> findByIdAndDeletedAtIsNull(Long id);
}
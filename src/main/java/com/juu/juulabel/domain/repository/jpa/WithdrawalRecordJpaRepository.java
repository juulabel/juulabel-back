package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.member.WithdrawalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRecordJpaRepository extends JpaRepository<WithdrawalRecord, Long> {
}

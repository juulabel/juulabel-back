package com.juu.juulabel.member.repository.jpa;

import com.juu.juulabel.member.domain.WithdrawalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRecordJpaRepository extends JpaRepository<WithdrawalRecord, Long> {
}

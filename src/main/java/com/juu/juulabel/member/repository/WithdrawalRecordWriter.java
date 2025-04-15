package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.jpa.WithdrawalRecordJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class WithdrawalRecordWriter {

    private final WithdrawalRecordJpaRepository withdrawalRecordJpaRepository;

    public void store(WithdrawalRecord withdrawalRecord) {
        withdrawalRecordJpaRepository.save(withdrawalRecord);
    }
}

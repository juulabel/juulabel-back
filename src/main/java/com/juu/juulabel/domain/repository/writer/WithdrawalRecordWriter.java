package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.member.WithdrawalRecord;
import com.juu.juulabel.domain.repository.jpa.WithdrawalRecordJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class WithdrawalRecordWriter {

    private final WithdrawalRecordJpaRepository withdrawalRecordJpaRepository;

    public void store(WithdrawalRecord withdrawalRecord) {
        withdrawalRecordJpaRepository.save(withdrawalRecord);
    }
}

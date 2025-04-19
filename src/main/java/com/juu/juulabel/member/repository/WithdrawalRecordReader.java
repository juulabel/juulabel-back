package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.member.repository.query.WithdrawalRecordQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class WithdrawalRecordReader {

    private final WithdrawalRecordQueryRepository withdrawalRecordQueryRepository;

    public boolean existEmail(String email) {
        return withdrawalRecordQueryRepository.existEmail(email);
    }
}

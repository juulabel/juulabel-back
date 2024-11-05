package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.repository.query.WithdrawalRecordQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class WithdrawalRecordReader {

    private final WithdrawalRecordQueryRepository withdrawalRecordQueryRepository;

    public boolean existEmail(String email) {
        return withdrawalRecordQueryRepository.existEmail(email);
    }
}

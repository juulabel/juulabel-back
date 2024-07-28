package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.domain.repository.reader.TastingNoteReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingNoteService {

    private final TastingNoteReader tastingNoteReader;


}

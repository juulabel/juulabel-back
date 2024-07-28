package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.repository.jpa.TastingNoteRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class TastingNoteWriter {

    private final TastingNoteRepository tastingNoteRepository;

}

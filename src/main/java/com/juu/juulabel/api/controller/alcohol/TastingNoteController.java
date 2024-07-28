package com.juu.juulabel.api.controller.alcohol;

import com.juu.juulabel.api.service.alcohol.TastingNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/notes"})
@RequiredArgsConstructor
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

}

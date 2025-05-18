package com.juu.juulabel.member.service;

import com.juu.juulabel.alcohol.domain.AlcoholicDrinks;
import com.juu.juulabel.alcohol.repository.AlcoholicDrinksReader;
import com.juu.juulabel.alcohol.repository.TastingNoteReader;
import com.juu.juulabel.alcohol.response.AlcoholicDrinksSummary;
import com.juu.juulabel.common.dto.request.MyAlcoholicDrinksListRequest;
import com.juu.juulabel.common.dto.request.TastingNoteListRequest;
import com.juu.juulabel.common.dto.response.DailyLifeListResponse;
import com.juu.juulabel.common.dto.response.MyAlcoholicDrinksListResponse;
import com.juu.juulabel.common.dto.response.MyDailyLifeListResponse;
import com.juu.juulabel.common.dto.response.MyTastingNoteListResponse;
import com.juu.juulabel.common.dto.response.TastingNoteListResponse;
import com.juu.juulabel.dailylife.repository.DailyLifeReader;
import com.juu.juulabel.dailylife.response.DailyLifeListRequest;
import com.juu.juulabel.dailylife.response.DailyLifeSummary;
import com.juu.juulabel.dailylife.response.MyDailyLifeSummary;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholicDrinks;
import com.juu.juulabel.member.repository.MemberAlcoholicDrinksReader;
import com.juu.juulabel.member.repository.MemberAlcoholicDrinksWriter;
import com.juu.juulabel.tastingnote.request.MyTastingNoteSummary;
import com.juu.juulabel.tastingnote.request.TastingNoteSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 회원 콘텐츠 관리 서비스
 * 일상생활, 시음노트, 전통주 관련 작업을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class MemberContentService {

    // 일상생활, 시음노트 관련
    private final DailyLifeReader dailyLifeReader;
    private final TastingNoteReader tastingNoteReader;
    
    // 전통주 관련
    private final AlcoholicDrinksReader alcoholicDrinksReader;
    private final MemberAlcoholicDrinksReader memberAlcoholicDrinksReader;
    private final MemberAlcoholicDrinksWriter memberAlcoholicDrinksWriter;

    // ===== 일상생활 관련 메소드 =====

    /**
     * 내가 작성한 일상생활 목록 조회
     */
    @Transactional(readOnly = true)
    public MyDailyLifeListResponse loadMyDailyLifeList(Member member, DailyLifeListRequest request) {
        Slice<MyDailyLifeSummary> myDailyLifeList = dailyLifeReader.getAllMyDailyLives(member,
                request.lastDailyLifeId(), request.pageSize());

        return new MyDailyLifeListResponse(myDailyLifeList);
    }

    /**
     * 특정 회원이 작성한 일상생활 목록 조회
     */
    @Transactional(readOnly = true)
    public DailyLifeListResponse loadMemberDailyLifeList(Member loginMember, DailyLifeListRequest request,
            Long memberId) {
        Slice<DailyLifeSummary> dailyLifeList = dailyLifeReader.getAllDailyLivesByMember(loginMember, memberId,
                request.lastDailyLifeId(), request.pageSize());

        return new DailyLifeListResponse(dailyLifeList);
    }

    // ===== 시음노트 관련 메소드 =====

    /**
     * 내가 작성한 시음노트 목록 조회
     */
    @Transactional(readOnly = true)
    public MyTastingNoteListResponse loadMyTastingNoteList(Member member, TastingNoteListRequest request) {
        Slice<MyTastingNoteSummary> myTastingNoteList = tastingNoteReader.getAllMyTastingNotes(member,
                request.lastTastingNoteId(), request.pageSize());

        return new MyTastingNoteListResponse(myTastingNoteList);
    }

    /**
     * 특정 회원이 작성한 시음노트 목록 조회
     */
    @Transactional(readOnly = true)
    public TastingNoteListResponse loadMemberTastingNoteList(Member loginMember, TastingNoteListRequest request,
            Long memberId) {
        Slice<TastingNoteSummary> tastingNoteList = tastingNoteReader.getAllTastingNotesByMember(loginMember, memberId,
                request.lastTastingNoteId(), request.pageSize());

        return new TastingNoteListResponse(tastingNoteList);
    }

    // ===== 전통주 관련 메소드 =====

    /**
     * 전통주 저장하기 또는 저장 취소
     * 
     * @return true if saved, false if unsaved
     */
    @Transactional
    public boolean saveAlcoholicDrinks(Member member, Long alcoholicDrinksId) {
        AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getById(alcoholicDrinksId);
        Optional<MemberAlcoholicDrinks> memberAlcoholicDrinks = memberAlcoholicDrinksReader
                .findByMemberAndAlcoholicDrinks(member, alcoholicDrinks);

        // 전통주가 이미 저장되어 있다면 삭제, 저장되어 있지 않다면 등록
        return memberAlcoholicDrinks
                .map(save -> {
                    memberAlcoholicDrinksWriter.delete(save);
                    return false;
                })
                .orElseGet(() -> {
                    memberAlcoholicDrinksWriter.store(member, alcoholicDrinks);
                    return true;
                });
    }

    /**
     * 내가 저장한 전통주 목록 조회
     */
    @Transactional(readOnly = true)
    public MyAlcoholicDrinksListResponse loadMyAlcoholicDrinks(Member member, MyAlcoholicDrinksListRequest request) {
        Slice<AlcoholicDrinksSummary> alcoholicDrinksSummaries = alcoholicDrinksReader.getAllMyAlcoholicDrinks(member,
                request.lastAlcoholicDrinksId(), request.pageSize());

        return new MyAlcoholicDrinksListResponse(alcoholicDrinksSummaries);
    }
} 
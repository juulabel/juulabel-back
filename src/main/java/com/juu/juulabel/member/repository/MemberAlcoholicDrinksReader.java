package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.domain.AlcoholicDrinks;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholicDrinks;
import com.juu.juulabel.member.repository.jpa.MemberAlcoholicDrinksJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class MemberAlcoholicDrinksReader {

    private final MemberAlcoholicDrinksJpaRepository memberAlcoholicDrinksJpaRepository;

    public Optional<MemberAlcoholicDrinks> findByMemberAndAlcoholicDrinks(Member member, AlcoholicDrinks alcoholicDrinks) {
        return memberAlcoholicDrinksJpaRepository.findByMemberAndAlcoholicDrinks(member, alcoholicDrinks);
    }
}
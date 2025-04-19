package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.alcohol.domain.AlcoholicDrinks;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholicDrinks;
import com.juu.juulabel.member.repository.jpa.MemberAlcoholicDrinksJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class MemberAlcoholicDrinksWriter {

    private final MemberAlcoholicDrinksJpaRepository memberAlcoholicDrinksJpaRepository;

    public void store(Member member, AlcoholicDrinks alcoholicDrinks) {
        MemberAlcoholicDrinks memberAlcoholicDrinks = MemberAlcoholicDrinks.create(member, alcoholicDrinks);
        memberAlcoholicDrinksJpaRepository.save(memberAlcoholicDrinks);
    }

    public void delete(MemberAlcoholicDrinks memberAlcoholicDrinks) {
        memberAlcoholicDrinksJpaRepository.delete(memberAlcoholicDrinks);
    }

}
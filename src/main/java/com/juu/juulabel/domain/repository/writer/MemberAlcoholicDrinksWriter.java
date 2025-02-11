package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.MemberAlcoholicDrinks;
import com.juu.juulabel.domain.repository.jpa.MemberAlcoholicDrinksJpaRepository;
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
package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.MemberAlcoholicDrinks;
import com.juu.juulabel.domain.repository.jpa.MemberAlcoholicDrinksJpaRepository;
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
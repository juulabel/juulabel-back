package com.juu.juulabel.member.repository.jpa;

import com.juu.juulabel.alcohol.domain.AlcoholicDrinks;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholicDrinks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAlcoholicDrinksJpaRepository extends JpaRepository<MemberAlcoholicDrinks, Long> {
    Optional<MemberAlcoholicDrinks> findByMemberAndAlcoholicDrinks(Member member, AlcoholicDrinks alcoholicDrinks);
}
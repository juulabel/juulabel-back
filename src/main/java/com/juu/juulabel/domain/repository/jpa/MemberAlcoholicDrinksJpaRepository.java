package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.MemberAlcoholicDrinks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAlcoholicDrinksJpaRepository extends JpaRepository<MemberAlcoholicDrinks, Long> {
    Optional<MemberAlcoholicDrinks> findByMemberAndAlcoholicDrinks(Member member, AlcoholicDrinks alcoholicDrinks);
}
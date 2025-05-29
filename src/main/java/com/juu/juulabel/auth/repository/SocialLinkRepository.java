package com.juu.juulabel.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.juu.juulabel.auth.domain.SocialLink;

public interface SocialLinkRepository extends CrudRepository<SocialLink, String> {

}

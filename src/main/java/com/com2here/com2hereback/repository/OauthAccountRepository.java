package com.com2here.com2hereback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.com2here.com2hereback.domain.OauthAccount; // ← 이 import도 필요

import java.util.Optional;

@Repository
public interface OauthAccountRepository extends JpaRepository<OauthAccount, Long> {
    Optional<OauthAccount> findByProviderAndOauthId(String provider, String oauthId);
}

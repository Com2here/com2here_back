package com.com2here.com2hereback.repository;


import com.com2here.com2hereback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 이메일로 사용자 찾기
    User findByEmail(String email);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    User findByUuid(String uuid);
}

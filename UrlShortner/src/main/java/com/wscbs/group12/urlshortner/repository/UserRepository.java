package com.wscbs.group12.urlshortner.repository;

import com.wscbs.group12.urlshortner.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);

    UserEntity findByUserIdAndPassword(String userId, String password);
}

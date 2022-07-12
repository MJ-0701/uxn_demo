package com.example.uxn_demo.doamain.user.domain.repository;

import com.example.uxn_demo.doamain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userID);

    Optional<User> findByUserIdAndPassword(String id, String password);
}

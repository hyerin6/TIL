package com.example.kakaologin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakaologin.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

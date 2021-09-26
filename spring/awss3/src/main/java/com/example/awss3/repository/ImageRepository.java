package com.example.awss3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.awss3.domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}

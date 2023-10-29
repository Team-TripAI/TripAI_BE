package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByImage(String image);
}

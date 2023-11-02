package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

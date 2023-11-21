package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.domain.Image;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepositoryCustom {
    List<Image> searchLabelMatch(List<String> labelList);
}

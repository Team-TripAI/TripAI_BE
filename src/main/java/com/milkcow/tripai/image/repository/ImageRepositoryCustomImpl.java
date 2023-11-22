package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.domain.QImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Image> searchLabelMatch(List<String> labelList) {
        QImage image = QImage.image;

        return queryFactory
                .select(image)
                .from(image)
                .where(
                        image.label1.in(labelList)
                                .or(image.label2.in(labelList))
                                .or(image.label3.in(labelList))
                                .or(image.label4.in(labelList))
                                .or(image.label5.in(labelList))
                ).fetch();
    }
}

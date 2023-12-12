package com.milkcow.tripai.image.service;

import com.milkcow.tripai.image.domain.Image;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageScoreTest {
    @Test
    public void 라벨_매치_수() throws Exception {
        //given
        List<String> labelList = List.of("Water", "Sky", "Cloud", "Boats and boating--Equipment and supplies",
                "Travel");
        Image image = Image.builder()
                .label1("a")
                .label2("Sky")
                .label3("Cloud")
                .label4("Water")
                .label5("b")
                .build();
        //when
        int matchLabelCount = ImageScore.getMatchLabelCount(labelList, image);
        //then
        Assertions.assertThat(matchLabelCount).isEqualTo(3);
    }
}
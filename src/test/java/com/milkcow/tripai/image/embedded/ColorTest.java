package com.milkcow.tripai.image.embedded;

import static org.junit.jupiter.api.Assertions.*;

import com.milkcow.tripai.image.service.ImageService;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

class ColorTest {

    @Test
    public void 벡터_크기() throws Exception{
        //given
        Color color = Color.builder().red(0).green(3).blue(4).build();
        //when
        double v = Color.calculateColorNorm(color);
        //then
        Assertions.assertThat(v).isEqualTo(5.0);
    }

    @Test
    public void 벡터_내적() throws Exception{
        //given
        Color c1 = Color.builder().red(1).green(2).blue(3).build();
        Color c2 = Color.builder().red(4).green(5).blue(6).build();
        //when
        double v = Color.calculateColorInnerProduct(c1, c2);
        //then
        Assertions.assertThat(v).isEqualTo(32.0);
    }

    @Test
    public void 코사인_유사도() throws Exception{
        //given
        Color c1 = Color.builder().red(191).green(199).blue(208).build();
        Color c2 = Color.builder().red(89).green(84).blue(63).build();

        double c1Norm = Color.calculateColorNorm(c1);
        double c2Norm = Color.calculateColorNorm(c2);
        double innerProduct = Color.calculateColorInnerProduct(c1, c2);
        //when
        double v = Color.calculateColorCosineSimilarity(c1, c2);
        //then
        System.out.println("similarity = " + v);
        Assertions.assertThat(v).isCloseTo(innerProduct/ c1Norm / c2Norm, Percentage.withPercentage(1));
    }


}
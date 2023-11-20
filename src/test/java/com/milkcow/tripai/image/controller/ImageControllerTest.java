package com.milkcow.tripai.image.controller;

import com.milkcow.tripai.image.exception.ImageException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageControllerTest {
    @Autowired
    private ImageController imageController;

    @Test
    public void 색상_유효성() throws Exception{
        //given
        List<String> colorList1 = List.of("abcd", "efgh");
        List<String> colorList2 = List.of("819CBA", "759BCC", "69809B", "A4C2E2", "405771");
        //when
        imageController.isValidHexColors(colorList2);
        Assertions.assertThatThrownBy(() -> imageController.isValidHexColors(colorList1)).isInstanceOf(ImageException.class);
    }

}
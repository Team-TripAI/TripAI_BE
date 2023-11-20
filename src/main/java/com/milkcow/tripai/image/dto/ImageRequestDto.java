package com.milkcow.tripai.image.dto;

import com.milkcow.tripai.image.embedded.Color;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageRequestDto {
    private List<String> labelList;
    private List<String> colorList;

    public List<Color> getColorList(){
        return colorList.stream().map(Color::stringToColor).collect(Collectors.toList());
    }
}

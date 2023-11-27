package com.milkcow.tripai.image.dto;

import com.milkcow.tripai.image.embedded.Color;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRequestDto {
    @ApiModelProperty(value = "라벨 리스트", example = "[\"Water\", \"Sky\", \"Cloud\", \"Boats and boating--Equipment and supplies\", \"Travel\"]")
    @Size(min = 5, max = 5)
    private List<String> labelList;

    @ApiModelProperty(value = "색상 리스트", example = "[\"819CBA\", \"759BCC\", \"69809B\", \"A4C2E2\", \"405771\"]")
    @Size(min = 5, max = 5)
    private List<String> colorList;


    public List<Color> stringToColor(){
        return colorList.stream().map(Color::stringToColor).collect(Collectors.toList());
    }
}

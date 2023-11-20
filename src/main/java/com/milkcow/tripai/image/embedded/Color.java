package com.milkcow.tripai.image.embedded;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Color {
    private int red;
    private int green;
    private int blue;


    public static Color stringToColor(String s){
        int red = Integer.decode("0x" + s.substring(0, 2));
        int green = Integer.decode("0x" + s.substring(2, 4));
        int blue = Integer.decode("0x" + s.substring(4, 6));

        return Color.builder().red(red).green(green).blue(blue).build();
    }
}

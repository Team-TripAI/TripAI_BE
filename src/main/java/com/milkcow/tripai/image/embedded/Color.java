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


    public static double calculateColorCosineSimilarity(Color c1, Color c2){
        double c1Norm = calculateColorNorm(c1);
        double c2Norm = calculateColorNorm(c2);
        double innerProduct = calculateColorInnerProduct(c1, c2);

        if(c1Norm == 0 || c2Norm == 0){
            return 0.0;
        }

        return innerProduct / (c1Norm * c2Norm);
    }

    public static double calculateColorNorm(Color color){
        double sum = 0.0;
        sum += color.getRed() * color.getRed();
        sum += color.getGreen() * color.getGreen();
        sum += color.getBlue() * color.getBlue();
        return Math.sqrt(sum);
    }

    public static double calculateColorInnerProduct(Color c1, Color c2) {
        double red = c1.getRed() * c2.getRed();
        double green = c1.getGreen() * c2.getGreen();
        double blue = c1.getBlue() * c2.getBlue();

        return red + green + blue;
    }
}

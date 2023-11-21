package com.milkcow.tripai.image.service;

import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.dto.ImageResponseData;
import com.milkcow.tripai.image.embedded.Color;
import java.util.List;

public class ImageScore {
    public Image image;
    public double score;

    public ImageScore(List<String> requestLabelList, List<Color> requestColorList, Image image) {
        this.image = image;
        this.score = calculateScore(requestLabelList, requestColorList, image);
        System.out.println("\tTotal Score: " + score);
    }

    public double getScore() {
        return score;
    }

    private double calculateScore(List<String> requestLabelList, List<Color> requestColorList, Image image) {
        int matchLabelCount = getMatchLabelCount(requestLabelList, image);
        List<Color> imageColorList = image.getColorList();
        double colorSimilaritySum = 0.0;

        for (Color reqColor : requestColorList) {
            for (Color imageColor : imageColorList) {
                colorSimilaritySum += Color.calculateColorCosineSimilarity(reqColor, imageColor);
            }
        }

        //TODO 디버깅을 위한 코드, 삭제 필요
        System.out.println("name: " + image.getLocationName());
        System.out.println("\tlabelCount: " + matchLabelCount);
        System.out.println("\tColor: " + colorSimilaritySum);
        return (double) matchLabelCount * 10 + colorSimilaritySum;
    }

    public static int getMatchLabelCount(List<String> labelList, Image image) {
        List<String> imageLabelList = image.getLabelList();
        return (int) labelList.stream()
                .filter(imageLabelList::contains)
                .count();
    }

    public ImageResponseData toDto() {
        return ImageResponseData.toDto(this.image);
    }
}

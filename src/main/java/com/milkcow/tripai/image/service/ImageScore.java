package com.milkcow.tripai.image.service;

import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.dto.ImageResponseData;
import com.milkcow.tripai.image.embedded.Color;
import java.util.List;

public class ImageScore {
    private static int MAX_SCORE = 75;

    public Image image;
    public double score;

    /**
     * ImageScore 생성자, score가 MAX_SCORE인 경우 같은 이미지로 판단하고 score를 0으로 초기화
     * @param requestLabelList 사용자 요청 라벨 리스트
     * @param requestColorList 사용자 요청 색상 리스트
     * @param image {@link Image}
     */
    public ImageScore(List<String> requestLabelList, List<Color> requestColorList, Image image) {
        this.image = image;
        this.score = calculateScore(requestLabelList, requestColorList, image);

        if(this.score == MAX_SCORE){
            this.score = 0;
        }
    }

    public double getScore() {
        return score;
    }

    /**
     * 사용자가 요청안 라벨, 색상과 DB에서 조회된 한개 이상이라도 라벨이 일치하는 이미지와 유사도 측정<p>
     * 점수 측정 방식<p>
     * 라벨 :
     *  일치하는 라벨의 수 * 10<p>
     * 색상:<p>
     *  - 사용자 요청 색상과 image의 각 색상별 코사인 유사도 측정<p>
     *  - 각 사용자 요청 색상 별 5개씩 총 25개의 코사인 유사도의 합<p>
     * 예시<p>
     *  일치하는라벨: 3 => 30<p>
     *  사용자 요청 색상: a, b, c, d, e<p>
     *  image의 color1: a, b, c, d, e
     *  <pre>   => 0.8 + 0.8 + 0.8 + 0.8 + 0.8 = 4.0</pre>
     *  ...<p>
     *  최종 점수: 30 + (4 + 3 + ...) = 54
     * @param requestLabelList 사용자 요청 라벨 리스트
     * @param requestColorList 사용자 요청 색상 리스트
     * @param image {@link Image}
     * @return 유사도 점수
     */
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

    /**
     * 사용자 요청 라벨 리스트중 image와 일치하는 라벨의 개수
     * @param labelList 사용자 요청 라벨 리스트
     * @param image {@link Image}
     * @return 일치 개수
     */
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

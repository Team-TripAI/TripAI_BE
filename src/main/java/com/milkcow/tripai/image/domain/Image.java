package com.milkcow.tripai.image.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private String formattedAddress;

    @Column(nullable = false)
    private String label1;

    @Column(nullable = false)
    private String label2;

    @Column(nullable = false)
    private String label3;

    @Column(nullable = false)
    private String label4;

    @Column(nullable = false)
    private String label5;

    @Column(nullable = false)
    private String color1;

    @Column(nullable = false)
    private String color2;

    @Column(nullable = false)
    private String color3;

    @Column(nullable = false)
    private String color4;

    @Column(nullable = false)
    private String color5;

    public void updateImage(Double lat, Double lng, String locationName, String formattedAddress,
                            List<String> labelList, List<String> colorList) {
        this.lat = lat;
        this.lng = lng;
        this.locationName = locationName;
        this.formattedAddress = formattedAddress;
        this.label1 = labelList.get(0);
        this.label2 = labelList.get(1);
        this.label3 = labelList.get(2);
        this.label4 = labelList.get(3);
        this.label5 = labelList.get(4);
        this.color1 = colorList.get(0);
        this.color2 = colorList.get(1);
        this.color3 = colorList.get(2);
        this.color4 = colorList.get(3);
        this.color5 = colorList.get(4);
    }
}

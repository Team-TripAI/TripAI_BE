package com.milkcow.tripai.image.domain;

import lombok.*;

import javax.persistence.*;

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
}

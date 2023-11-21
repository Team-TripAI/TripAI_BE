package com.milkcow.tripai.image.domain;

import com.milkcow.tripai.image.embedded.Color;
import java.util.ArrayList;
import java.util.List;
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
    private String uuid;

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

    public List<Color> getColorList(){
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.stringToColor(color1));
        colors.add(Color.stringToColor(color2));
        colors.add(Color.stringToColor(color3));
        colors.add(Color.stringToColor(color4));
        colors.add(Color.stringToColor(color5));
        return colors;
    }

    public List<String> getLabelList(){
        return List.of(label1, label2, label3, label4, label5);
    }
}

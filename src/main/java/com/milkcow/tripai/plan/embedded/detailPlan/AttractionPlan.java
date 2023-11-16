package com.milkcow.tripai.plan.embedded.detailPlan;

import com.milkcow.tripai.plan.embedded.PlaceHour;
import java.util.List;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionPlan extends CommonDetailPlan{
    private List<PlaceHour> hours;
    private String image;
}

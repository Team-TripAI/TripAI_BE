package com.milkcow.tripai.plan.embedded;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionOffer {
    @ApiModelProperty(value = "활동 명", example = "Tokyo Full-Day Sightseeing Tour by Coach with Lunch Option")
    private String offerName;
    @ApiModelProperty(value = "활동 가격", example = "140431")
    private Integer offerPrice;
    @ApiModelProperty(value = "활동 예약 url", example = "https://www.tripadvisor.com/Commerce?url=https%3A%2F%2Fwww.viator.com%2Ftours%2FTokyo%2F1-Day-Amazing-Tokyo-Bus-Tour%2Fd334-28575P2%3Feap%3Dmobile-app-11383%26aid%3Dtripenandr&partnerKey=1&urlKey=5d10102dd7480d01d&logme=true&uidparam=refid&attrc=true&Provider=Viator&area=viator_multi&slot=1&cnt=1&geo=1373780&clt=TM&from=api&nt=true")
    private String offerUrl;
}

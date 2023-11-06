package com.milkcow.tripai.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginDto {
    @ApiModelProperty(value = "구글 OAuth2 access token", example = "ya29.a0AfB_byAOBWrBEY1JwreZt4AdoS9lNlqg...")
    private String token;
}

package com.milkcow.tripai.member.dto;

import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateRequestDto {

    private Optional<String> nickname;

    private String pw;

}

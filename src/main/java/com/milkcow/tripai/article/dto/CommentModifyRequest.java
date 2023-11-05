package com.milkcow.tripai.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CommentModifyRequest {

    @NotNull
    @Size(max = 200)
    private final String content;
}

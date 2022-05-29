package com.example.demo.src.store.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PatchReviewReq {
    @NotNull(message = "별점 개수를 입력하세요")
    private Integer review_star;

    private String review_content;
}

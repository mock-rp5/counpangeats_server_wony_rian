package com.example.demo.src.store.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PostHelpReq {
    @NotNull(message = "리뷰 식별자를 입력하세요")
    private Integer review_id;
    @NotNull(message = "도움 표시를 입력하세요")
    private String help_sign_value;
}

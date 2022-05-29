package com.example.demo.src.store.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchHelpReq {
    @NotNull(message = "리뷰 식별자를 입력하세요")
    private Integer review_id;
}

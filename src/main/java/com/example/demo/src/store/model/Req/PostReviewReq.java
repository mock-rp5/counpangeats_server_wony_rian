package com.example.demo.src.store.model.Req;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    @NotNull(message = "주문 정보 식별자를 입력하세요.")
    private Integer order_info_id;

    @NotNull(message = "별점 개수를 입력하세요")
    private Integer review_star;

    private String review_content;
    private String review_image_url;

    private Integer is_delivery_good;
}

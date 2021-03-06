package com.example.demo.src.store.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRes {
    private Integer review_id;
    private Integer review_star;
    private String review_image_url;
    private String review_content;
}

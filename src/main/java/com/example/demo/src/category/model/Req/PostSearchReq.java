package com.example.demo.src.category.model.Req;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonAutoDetect
@NoArgsConstructor
public class PostSearchReq {
    private String category_name;
}

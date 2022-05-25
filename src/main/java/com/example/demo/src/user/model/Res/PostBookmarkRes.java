package com.example.demo.src.user.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBookmarkRes {
    private int bookmark_id;
    private int user_id;
    private int store_id;

}

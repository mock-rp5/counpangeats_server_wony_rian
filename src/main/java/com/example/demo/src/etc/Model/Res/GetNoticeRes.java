package com.example.demo.src.etc.Model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNoticeRes {
    private String created_at;
    private String notice_name;
    private String notice_content;
}

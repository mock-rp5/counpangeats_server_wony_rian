package com.example.demo.src.push;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestDto {
    private String targetToken;
    private String title;
    private String body;
}

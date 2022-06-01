package com.example.demo.src.etc.Model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetQuestionRes {
    private String category_name;
    private String question_name;
    private String question_answer;
}

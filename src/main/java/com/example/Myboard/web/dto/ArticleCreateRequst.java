package com.example.Myboard.web.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ArticleCreateRequst {
    @NotBlank(message = "제목은 넣어라 휴먼")
    private String title;

    @NotBlank(message = "내용을 넣어라 휴먼!")
    private String content;
}

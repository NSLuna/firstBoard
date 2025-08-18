package com.example.Myboard.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleUpdateRequest {
    @NotBlank(message = "제목은 필수다 아그야")
    private String title;
    @NotBlank(message = "내용은 필수다 아그야")
    private String content;
}

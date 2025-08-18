package com.example.Myboard.web.api;


import com.example.Myboard.domain.Article;
import com.example.Myboard.service.ArticleService;
import com.example.Myboard.web.dto.ArticleCreateRequst;
import com.example.Myboard.web.dto.ArticleUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleApiController {
    private final ArticleService articleService;

    @PostMapping                                                                      //↓↓↓↓스프링이 낚아채간 유저 정보
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleCreateRequst req, Authentication auth) {
        Long id = articleService.create(req, auth.getName());
        return ResponseEntity.created(URI.create("/articles/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody ArticleUpdateRequest req,
                                       Authentication auth) {
        articleService.update(id, auth.getName(), req);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        articleService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

}

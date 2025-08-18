package com.example.Myboard.service;

import com.example.Myboard.domain.Article;
import com.example.Myboard.repositiory.ArticleRepository;
import com.example.Myboard.repositiory.UserRepository;
import com.example.Myboard.web.dto.ArticleCreateRequst;
import com.example.Myboard.web.dto.ArticleUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    private final UserRepository userRepository;

//    @Transactional
//    public void create(ArticleCreateRequst req, String email){
//        articleRepository.save(Article.builder().
//                title(req.getTitle())
//                .content(req.getContent())
//                .build());
//    }

    @Transactional
    public Long create(ArticleCreateRequst req, String email){
        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return articleRepository.save(
                Article.builder()
                        .title(req.getTitle())
                        .content(req.getContent())
                        .author(author)
                        .build()
        ).getId();
    }

    public Article findById(Long id){
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found :" + id));
    }
//    @Transactional
//    public void update(Long id, ArticleUpdateRequest req){
//        var article = findById(id);
//        article.update(req.getTitle(), article.getContent());
//    }
    @Transactional
    public void update(Long id, String email, ArticleUpdateRequest req){
        var article = articleRepository.findByIdAndAuthor_Email(id, email)
                .orElseThrow(() -> new AccessDeniedException("본인 글이 아닙니다."));

        article.update(req.getTitle(), req.getContent());
    }

//    @Transactional
//    public void delete(Long id){
//        articleRepository.deleteById(id);
//    }

    @Transactional
    public void delete(Long id, String email){

        if (articleRepository.deleteByIdAndAuthor_Email(id, email) == 0)
            throw new AccessDeniedException("본인 글이 아닙니다.");
    }

//    public Long create(ArticleCreateRequst req) {
//    }
}






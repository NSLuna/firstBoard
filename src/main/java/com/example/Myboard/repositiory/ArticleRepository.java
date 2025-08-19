package com.example.Myboard.repositiory;

import com.example.Myboard.domain.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


//    public interface ArticleRepository extends JpaRepository<Article, Long> {
//        Optional<Article> findByIdAndAuthor_Email(Long id, String email);
//        long deleteByIdAndAuthor_Email(Long id, String email);
//
//}
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @EntityGraph(attributePaths = "author")
    Optional<Article> findById(Long id);

    @EntityGraph(attributePaths = "author")
    Optional<Article> findByIdAndAuthor_Email(Long id, String email);

    long deleteByIdAndAuthor_Email(Long id, String email);

    @Query("select a from Article a join fetch a.author")
    List<Article> findAllWithAuthor();
}
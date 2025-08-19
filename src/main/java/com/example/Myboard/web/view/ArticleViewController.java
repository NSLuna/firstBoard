package com.example.Myboard.web.view;

import com.example.Myboard.service.ArticleService;
import com.example.Myboard.web.dto.ArticleCreateRequst;
import com.example.Myboard.web.dto.ArticleUpdateRequest;
import com.example.Myboard.web.dto.PostView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleViewController {

    private final ArticleService articleService;

    // === (기존 화면들) 이제 블로그로 대체하니까 주석 처리 ===
//    @GetMapping("/articles")
//    public String list(Model model) {
//        model.addAttribute("articles", articleService.findAll());
//        return "article-list";
//    }
//
//    @GetMapping("/articles/new")
//    public String createForm() {
//        return "article-form";
//    }
//
//    @GetMapping("/articles/{id}")
//    public String detail(@PathVariable Long id, Model model, Authentication auth) {
//        var article = articleService.findById(id);
//        model.addAttribute("article", article);
//        boolean isOwner = auth != null && article.getAuthor().getEmail().equals(auth.getName());
//        model.addAttribute("isOwner", isOwner);
//        return "article-detail";
//    }
//
//    @GetMapping("/articles/{id}/edit")
//    public String editForm(@PathVariable Long id, Model model) {
//        var article = articleService.findById(id);
//        model.addAttribute("article", article);
//        return "article-edit";
//    }


        // === 블로그 메인 화면 ===
        @GetMapping("/blog")
        public String blog(Model model) {
            var posts = articleService.findAll().stream()
                    .map(a -> new PostView(
                            a.getId(),
                            a.getTitle(),
                            a.getAuthor() != null ? a.getAuthor().getName() : "익명",
                            null,          // createdAt 아직 없으면 null
                            List.of()      // tags 아직 없으면 빈 리스트
                    ))
                    .toList();

            model.addAttribute("posts", posts);
            return "blog"; // -> templates/blog.html
        }

        // === 새 글 작성 폼 ===
        @GetMapping("/articles/new")
        public String createForm() {
            return "article-form"; // -> templates/article-form.html
        }

    @PostMapping("/articles")
    public String createArticle(@ModelAttribute ArticleCreateRequst req, Authentication auth) {
        Long id = articleService.create(req, auth.getName());
        return "redirect:/blog";
    }

        // === 기존 경로들: 블로그로 리다이렉트 ===
        @GetMapping("/articles")
        public String oldListRedirect() {
            return "redirect:/blog";
        }

//        @GetMapping("/articles/{id}")
//        public String detailRedirect(@PathVariable Long id) {
//            return "redirect:/blog";
//        }
@GetMapping("/articles/{id}")
public String detail(@PathVariable Long id, Model model, Authentication auth) {
    var article = articleService.findById(id); // author는 EntityGraph로 함께 로딩됨
    model.addAttribute("article", article);

    String loginEmail = (auth != null) ? auth.getName() : null;
    String authorEmail = (article.getAuthor() != null) ? article.getAuthor().getEmail() : null;
    boolean isOwner = (loginEmail != null && loginEmail.equals(authorEmail));
    model.addAttribute("isOwner", isOwner);

    String displayName =
            (auth != null) ? auth.getName()
                    : (article.getAuthor() != null ? article.getAuthor().getEmail() : "방문자");
    model.addAttribute("displayName", displayName);

    return "article-detail";
}


    @GetMapping("/articles/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var article = articleService.findById(id);   // author까지 로딩됨
        model.addAttribute("article", article);
        return "article-edit"; // -> templates/article-edit.html
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteFromView(@PathVariable Long id, Authentication auth) {
        articleService.delete(id, auth.getName());
        return "redirect:/blog";
    }
    @PostMapping("/articles/{id}/edit")
    public String updateFromView(@PathVariable Long id,
                                 @ModelAttribute ArticleUpdateRequest req,
                                 Authentication auth) {
        articleService.update(id, auth.getName(), req);
        return "redirect:/articles/" + id; // 저장 후 상세로
    }


}



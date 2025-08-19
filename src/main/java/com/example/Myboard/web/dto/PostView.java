package com.example.Myboard.web.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PostView {

    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;
    private final List<String> tags;

    public PostView(Long id, String title, String author,
                    LocalDateTime createdAt, List<String> tags) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.tags = (tags != null) ? tags : List.of();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getTags() {
        return tags;
    }
}

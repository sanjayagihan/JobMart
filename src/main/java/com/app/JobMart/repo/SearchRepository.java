package com.app.JobMart.repo;

import com.app.JobMart.model.Post;

import java.util.List;

public interface SearchRepository {
    List<Post> findByText(String text);
}

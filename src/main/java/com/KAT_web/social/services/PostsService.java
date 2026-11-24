package com.KAT_web.social.services;

import com.KAT_web.social.models.Posts;
import com.KAT_web.social.repository.PostsRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class PostsService {
    private final PostsRepo postsRepo;

    public PostsService(PostsRepo postsRepo) { this.postsRepo = postsRepo; }
    public Posts savePost(Posts post) {
        if (post.getCreated() == null) post.setCreated(LocalDateTime.now());
        return postsRepo.save(post);
    }
    public List<Posts> getAllPosts() { return postsRepo.findAll(); }
    public Optional<Posts> getPostById(Long id) { return postsRepo.findById(id); }
    public void deletePost(Long id) { postsRepo.deleteById(id); }
}

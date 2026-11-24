package com.KAT_web.social.controllers;

import com.KAT_web.social.models.Posts;
import com.KAT_web.social.services.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PostsController {
    private final PostsService postsService;

    public PostsController(PostsService postsService) { this.postsService = postsService; }

    @PostMapping
    public ResponseEntity<Posts> createPost(@RequestBody Posts post) { return ResponseEntity.ok(postsService.savePost(post)); }

    @GetMapping
    public List<Posts> getPosts() { return postsService.getAllPosts(); }

    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPost(@PathVariable Long id) {
        return postsService.getPostById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (postsService.getPostById(id).isEmpty()) return ResponseEntity.notFound().build();
        postsService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

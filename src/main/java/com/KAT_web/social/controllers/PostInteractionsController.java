package com.KAT_web.social.controllers;

import com.KAT_web.social.models.PostComment;
import com.KAT_web.social.models.PostLike;
import com.KAT_web.social.repository.PostCommentRepo;
import com.KAT_web.social.repository.PostLikeRepo;
import com.KAT_web.social.services.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PostInteractionsController {
    private final PostCommentRepo comments;
    private final PostLikeRepo likes;
    private final PostsService posts;

    public PostInteractionsController(PostCommentRepo comments, PostLikeRepo likes, PostsService posts) {
        this.comments = comments;
        this.likes = likes;
        this.posts = posts;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        if (posts.getPostById(postId).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(comments.findByPostIdOrderByCreatedAsc(postId));
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        if (posts.getPostById(postId).isEmpty()) return ResponseEntity.notFound().build();
        if (request.userId() == null || request.content() == null || request.content().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "A signed-in user and comment are required."));
        }
        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setUserId(request.userId());
        comment.setContent(request.content().trim());
        comment.setCreated(LocalDateTime.now());
        return ResponseEntity.ok(comments.save(comment));
    }

    @GetMapping("/likes")
    public ResponseEntity<?> getLikes(@PathVariable Long postId, @RequestParam Long userId) {
        if (posts.getPostById(postId).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(likeStatus(postId, userId));
    }

    @PostMapping("/likes")
    public ResponseEntity<?> like(@PathVariable Long postId, @RequestParam Long userId) {
        if (posts.getPostById(postId).isEmpty()) return ResponseEntity.notFound().build();
        if (!likes.existsByPostIdAndUserId(postId, userId)) {
            PostLike like = new PostLike();
            like.setPostId(postId);
            like.setUserId(userId);
            likes.save(like);
        }
        return ResponseEntity.ok(likeStatus(postId, userId));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<?> unlike(@PathVariable Long postId, @RequestParam Long userId) {
        if (posts.getPostById(postId).isEmpty()) return ResponseEntity.notFound().build();
        likes.findByPostIdAndUserId(postId, userId).ifPresent(likes::delete);
        return ResponseEntity.ok(likeStatus(postId, userId));
    }

    private LikeStatus likeStatus(Long postId, Long userId) {
        return new LikeStatus(likes.countByPostId(postId), likes.existsByPostIdAndUserId(postId, userId));
    }

    public record CommentRequest(Long userId, String content) {}
    public record LikeStatus(long count, boolean liked) {}
}

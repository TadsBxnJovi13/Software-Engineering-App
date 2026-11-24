package com.KAT_web.social.repository;

import com.KAT_web.social.models.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostCommentRepo extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostIdOrderByCreatedAsc(Long postId);
}

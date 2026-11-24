package com.KAT_web.social.repository;

import com.KAT_web.social.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepo extends JpaRepository<Posts, Long> {
}

package com.instagram.repository;

import com.instagram.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {

    Comment findByCommentId(UUID commentId);
}

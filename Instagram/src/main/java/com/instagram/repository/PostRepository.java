package com.instagram.repository;

import com.instagram.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Posts, UUID> {

    Posts findByPostId(UUID postId);
}

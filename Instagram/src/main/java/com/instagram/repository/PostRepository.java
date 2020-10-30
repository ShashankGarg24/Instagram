package com.instagram.repository;

import com.instagram.models.Posts;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PostRepository extends CrudRepository<Posts, UUID> {
}

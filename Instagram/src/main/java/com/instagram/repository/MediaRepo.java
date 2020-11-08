package com.instagram.repository;

import com.instagram.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface MediaRepo extends JpaRepository<Media, UUID> {
    List<Media> findAllByPostId(UUID postId);
}

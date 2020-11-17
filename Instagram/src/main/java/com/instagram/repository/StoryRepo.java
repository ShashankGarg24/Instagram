package com.instagram.repository;

import com.instagram.models.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StoryRepo extends JpaRepository<Story, UUID> {

    List<Story> findAllByStoryCreationTimeStamp(LocalDateTime currentDateTime);
}

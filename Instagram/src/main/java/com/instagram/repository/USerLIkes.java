package com.instagram.repository;

import com.instagram.models.LikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface USerLIkes extends JpaRepository<LikeModel, UUID> {

    LikeModel findByProfileIdAndAndContentId(UUID profileId, UUID contentId);
    List<LikeModel> findAllByContentId(UUID contentId);
}

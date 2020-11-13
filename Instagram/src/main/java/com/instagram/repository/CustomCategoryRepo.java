package com.instagram.repository;

import com.google.common.cache.CacheBuilder;
import com.instagram.models.CustomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomCategoryRepo extends JpaRepository<CustomCategory, UUID> {

    CustomCategory findByCategoryId(UUID categoryId);
   // CustomCategory findByPrimaryTypeAndProfileProfileId(boolean type, UUID profileId);
   // CustomCategory findByCategoryIdAndProfileProfileId(UUID categoryId, UUID profileId);

}

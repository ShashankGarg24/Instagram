package com.instagram.repository;

import com.instagram.models.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProfileRepository extends CrudRepository<UserProfile, UUID> {

    UserProfile findByUsername(String username);
    UserProfile findByProfileId(UUID id);
}

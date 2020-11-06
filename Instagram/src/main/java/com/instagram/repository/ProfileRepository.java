package com.instagram.repository;

import com.instagram.models.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfileRepository extends CrudRepository<UserProfile, UUID> {

    UserProfile findByUsername(String username);
    UserProfile findByProfileId(UUID id);
}

package com.instagram.repository;

import com.instagram.models.Posts;
import com.instagram.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findByUsername(String username);
    UserProfile findByProfileId(UUID id);
    List<UserProfile> findAllByUserUserId(UUID userId);
    List<UserProfile> findByOrderByFollowingNumberDesc();

    @Query("Select p from UserProfile p where lower(p.fullName) like lower(concat('%', ?1, '%'))"
            + " or lower(p.username) like lower(concat('%', ?1, '%'))")
    List<UserProfile> findAll(String keyword);
}

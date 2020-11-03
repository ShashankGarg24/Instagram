package com.instagram.repository;

import com.instagram.models.Media;
import com.instagram.models.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByUsername(String username);

    User findByUserEmail(String userEmail);

    User findByVerificationToken(String token);

    User findByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.fullName = ?1 , u.username = ?2 , u.userBio = ?3 WHERE u.userId= ?4")
    void updateInitialDetails(String fullName, String username, String userBio, UUID userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.userPrivacy = ?1 WHERE u.username = ?2 ")
    void updatePrivacy(String privacy, String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.profilePic = null WHERE u.userId = ?1 ")
    void setProfilePicToNull(UUID userId);


}

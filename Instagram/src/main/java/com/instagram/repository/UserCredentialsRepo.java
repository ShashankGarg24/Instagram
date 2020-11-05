package com.instagram.repository;


import com.instagram.models.User;
import com.instagram.models.UserCredentials;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface UserCredentialsRepo extends CrudRepository<UserCredentials, UUID> {

    UserCredentials findByUserEmail(String userEmail);

    UserCredentials findByProfilesProfileId(UUID profileId);

    @Transactional
    @Modifying
    @Query("UPDATE UserCredentials u SET u.verified = true WHERE u.userEmail = ?1 ")
    void verifyUser(String userEmail);




}

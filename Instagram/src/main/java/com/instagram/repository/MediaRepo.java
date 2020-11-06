package com.instagram.repository;

import com.instagram.models.Media;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface MediaRepo extends CrudRepository<Media, UUID> {

}

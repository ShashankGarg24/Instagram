package com.instagram.repository;

import com.instagram.models.Media;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MediaRepo extends CrudRepository<Media, UUID> {

}

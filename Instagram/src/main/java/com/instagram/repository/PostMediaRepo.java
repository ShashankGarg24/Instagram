package com.instagram.repository;

import com.instagram.models.PostMedia;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PostMediaRepo extends CrudRepository<PostMedia, UUID> {

}

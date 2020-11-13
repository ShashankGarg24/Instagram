package com.instagram.repository;

import com.instagram.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReplyRepo extends JpaRepository<Reply, UUID> {

    Reply findByReplyId(UUID replyId);
}

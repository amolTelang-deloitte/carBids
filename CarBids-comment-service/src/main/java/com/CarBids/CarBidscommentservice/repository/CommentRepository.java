package com.CarBids.CarBidscommentservice.repository;

import com.CarBids.CarBidscommentservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllBylotId(Long lotId);
}

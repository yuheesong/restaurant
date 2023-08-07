package com.restaurant.repository;


import com.restaurant.entity.Board;
import com.restaurant.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}

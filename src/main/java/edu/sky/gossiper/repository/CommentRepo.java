package edu.sky.gossiper.repository;

import edu.sky.gossiper.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}

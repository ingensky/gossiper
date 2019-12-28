package edu.sky.gossiper.service;

import edu.sky.gossiper.domain.Comment;
import edu.sky.gossiper.domain.User;
import edu.sky.gossiper.repository.CommentRepo;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepo commentRepo;

    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        commentRepo.save(comment);
        return comment;
    }
}

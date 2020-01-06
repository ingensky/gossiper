package edu.sky.gossiper.service;

import edu.sky.gossiper.domain.Comment;
import edu.sky.gossiper.domain.User;
import edu.sky.gossiper.domain.Views;
import edu.sky.gossiper.dto.EventType;
import edu.sky.gossiper.dto.ObjectType;
import edu.sky.gossiper.repository.CommentRepo;
import edu.sky.gossiper.util.WsSender;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final BiConsumer<EventType, Comment> wsSender;


    public CommentService(CommentRepo commentRepo, WsSender wsSender) {
        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);

    }

    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        Comment commentFromDB = commentRepo.save(comment);
        wsSender.accept(EventType.CREATE, commentFromDB);

        return commentFromDB;
    }
}

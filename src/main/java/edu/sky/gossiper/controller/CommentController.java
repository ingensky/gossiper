package edu.sky.gossiper.controller;

import edu.sky.gossiper.domain.Comment;
import edu.sky.gossiper.domain.User;
import edu.sky.gossiper.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal User user
    ) {
        return commentService.create(comment, user);
    }
}

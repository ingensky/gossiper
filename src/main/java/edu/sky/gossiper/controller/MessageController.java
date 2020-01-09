package edu.sky.gossiper.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sky.gossiper.domain.Message;
import edu.sky.gossiper.domain.User;
import edu.sky.gossiper.domain.Views;
import edu.sky.gossiper.dto.MessagePageDto;
import edu.sky.gossiper.service.MessageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("message")
public class MessageController {

    public static final int MESSAGES_PER_PAGE = 3;
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            ) {
        return messageService.findAll(pageable);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getMessage(
            @PathVariable("id") Message message
    ) {
        return message;
    }

    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return messageService.create(message, user);
    }


    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDB,
            @RequestBody Message message
    ) throws IOException {
        return messageService.update(messageFromDB, message);
    }

    @DeleteMapping("{id}")
    public void deleteMessage(
            @PathVariable("id") Message message
    ) {
        messageService.delete(message);
    }


}

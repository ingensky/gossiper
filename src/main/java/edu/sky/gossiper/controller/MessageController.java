package edu.sky.gossiper.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sky.gossiper.domain.Message;
import edu.sky.gossiper.domain.Views;
import edu.sky.gossiper.repository.MessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public List<Message> list() {
        return messageRepository.findAll();
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
            @RequestBody Message message
    ) {
        message.setCreationTimestamp(LocalDateTime.now());
        return  messageRepository.save(message);
    }


    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDB,
            @RequestBody Message message
    ) {

        BeanUtils.copyProperties(message, messageFromDB, "id");
        return messageRepository.save(messageFromDB);
    }

    @DeleteMapping("{id}")
    public void deleteMessage(
            @PathVariable("id") Message message
    ) {
        messageRepository.delete(message);
    }

    @MessageMapping("/changeMessage")
    @SendTo("/topic/activity")
    public Message change(Message message) {
        message.setCreationTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }


}

package edu.sky.gossiper.controller;

import edu.sky.gossiper.util.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("message")
public class MessageController {

    private AtomicInteger counter = new AtomicInteger(10);

    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{
            put("id", "1");
            put("text", "first message");
        }});
        add(new HashMap<String, String>() {{
            put("id", "2");
            put("text", "second message");
        }});
        add(new HashMap<String, String>() {{
            put("id", "3");
            put("text", "third message");
        }});
    }};


    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getMessage(
            @PathVariable String id
    ) {
        return getMessageById(id);
    }

    @PostMapping
    public Map<String, String> create(
            @RequestBody Map<String, String> message
    ) {
        message.put("id", String.valueOf(counter.incrementAndGet()));
        messages.add(message);
        return message;

    }


    @PutMapping("{id}")
    public Map<String, String> update(
            @PathVariable String id,
            @RequestBody Map<String, String> message
    ) {
        Map<String, String> messageById = getMessageById(id);
        messageById.putAll(message);
        messageById.put("id", id);
        return messageById;
    }

    @DeleteMapping("{id}")
    public void deleteMessage(
            @PathVariable String id
    ) {
        Map<String, String> messageById = getMessageById(id);
        messages.remove(messageById);
    }

    private Map<String, String> getMessageById(@PathVariable String id) {
        return messages.stream()
                .filter(e -> e.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}

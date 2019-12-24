package edu.sky.gossiper.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sky.gossiper.domain.Message;
import edu.sky.gossiper.domain.Views;
import edu.sky.gossiper.dto.EventType;
import edu.sky.gossiper.dto.MetaDto;
import edu.sky.gossiper.dto.ObjectType;
import edu.sky.gossiper.repository.MessageRepository;
import edu.sky.gossiper.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("message")
public class MessageController {

    public static final String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/?=&#]+";
    public static final String IMAGE_PATTERN = "\\.(jpg|png|bmp|gif)$";

    public static final Pattern URL_REGEXP = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    public static final Pattern IMG_REGEXP = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    public MessageController(MessageRepository messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public List<Message> list() {
        return messageRepo.findAll();
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
    ) throws IOException {
        message.setCreationTimestamp(LocalDateTime.now());
        fillMeta(message);
        Message createdMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, createdMessage);

        return createdMessage;
    }


    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDB,
            @RequestBody Message message
    ) throws IOException {

        BeanUtils.copyProperties(message, messageFromDB, "id");
        fillMeta(messageFromDB);
        Message updatedMessage = messageRepo.save(messageFromDB);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void deleteMessage(
            @PathVariable("id") Message message
    ) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    private void fillMeta(Message message) throws IOException {
        String text = message.getText();

        Matcher matcher = URL_REGEXP.matcher(text);
        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());
            message.setLink(url);

            matcher = IMG_REGEXP.matcher(url);
            if (matcher.find()) {
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {

                MetaDto meta = getMeta(url);

                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }

        }

    }

    private MetaDto getMeta(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements title = document.select("meta[name$=title], meta[property$=title]");
        Elements description = document.select("meta[name$=description], meta[property$=description]");
        Elements cover = document.select("meta[name$=image], meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }

}

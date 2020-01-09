package edu.sky.gossiper.service;

import edu.sky.gossiper.domain.Message;
import edu.sky.gossiper.domain.User;
import edu.sky.gossiper.domain.Views;
import edu.sky.gossiper.dto.EventType;
import edu.sky.gossiper.dto.MessagePageDto;
import edu.sky.gossiper.dto.MetaDto;
import edu.sky.gossiper.dto.ObjectType;
import edu.sky.gossiper.repository.MessageRepository;
import edu.sky.gossiper.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageService {
    public static final String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/?=&#]+";
    public static final String IMAGE_PATTERN = "\\.(jpg|png|bmp|gif)$";

    public static final Pattern URL_REGEXP = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    public static final Pattern IMG_REGEXP = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    public MessageService(MessageRepository messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
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

        String coverContent = getContent(cover.first());
        coverContent = coverContent.startsWith("http") ? coverContent : url + coverContent;
        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                coverContent
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }

    public void delete(Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    public Message update(Message messageFromDB, Message message) throws IOException {
        BeanUtils.copyProperties(message, messageFromDB, "id");
        fillMeta(messageFromDB);
        Message updatedMessage = messageRepo.save(messageFromDB);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;

    }

    public Message create(Message message, User user) throws IOException {
        message.setCreationTimestamp(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);
        Message createdMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, createdMessage);

        return createdMessage;

    }

    public MessagePageDto findAll(Pageable pageable) {

        Page<Message> messagePage = messageRepo.findAll(pageable);
        return new MessagePageDto(messagePage.getContent(), pageable.getPageNumber(), messagePage.getTotalPages());
    }
}

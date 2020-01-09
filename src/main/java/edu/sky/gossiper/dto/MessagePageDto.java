package edu.sky.gossiper.dto;

import com.fasterxml.jackson.annotation.JsonView;
import edu.sky.gossiper.domain.Message;
import edu.sky.gossiper.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto {
    private List<Message> messages;
    private int currentPage;
    private int totalPages;
}

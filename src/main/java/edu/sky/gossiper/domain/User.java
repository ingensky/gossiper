package edu.sky.gossiper.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "usr")
@Data
public class User {

    @Id
    public String id;

    private String name;

    private String email;

    private LocalDateTime lastVisit;

}

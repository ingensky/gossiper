package edu.sky.gossiper.repository;

import edu.sky.gossiper.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

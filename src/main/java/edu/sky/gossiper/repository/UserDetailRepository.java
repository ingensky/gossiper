package edu.sky.gossiper.repository;

import edu.sky.gossiper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<User, String> {
}

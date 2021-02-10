package com.nhn.rookie8.movieswanuserapi.repository;

import com.nhn.rookie8.movieswanuserapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

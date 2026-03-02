package com.yoshi.sleep_log.domain.repository;

import com.yoshi.sleep_log.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
}

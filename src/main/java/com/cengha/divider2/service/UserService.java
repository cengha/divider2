package com.cengha.divider2.service;

import com.cengha.divider2.exception.UserNameAlreadyTakenException;
import com.cengha.divider2.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    User findOne(Long id);

    User updateUserAsDisabled(Long userId);

    User create(String username);

}

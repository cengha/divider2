package com.cengha.divider2.service;

import com.cengha.divider2.model.User;

public interface UserService {

    User save(User user);

    User findOne(Long id);

    User updateUserAsDisabled(Long userId);

    User create(String username);

}

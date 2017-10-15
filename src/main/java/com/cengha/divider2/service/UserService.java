package com.cengha.divider2.service;

import com.cengha.divider2.exception.UserNameAlreadyTakenException;
import com.cengha.divider2.model.User;
import com.cengha.divider2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by TRKoseCe on 20.07.2017.
 */
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User findOne(Long id) {
        return repository.findOne(id);
    }

    public User updateUserAsDisabled(Long userId) {
        User one = this.findOne(userId);
        one.setEnabled(false);
        return this.save(one);
    }

    public User create(String username) {

        Optional<User> optional = repository.getFirstByUsernameAndEnabledIsTrue(username);
        if (optional.isPresent()) {
            throw new UserNameAlreadyTakenException();
        }

        User user = new User();
        user.setUsername(username);
        user = this.save(user);

        return user;
    }

}

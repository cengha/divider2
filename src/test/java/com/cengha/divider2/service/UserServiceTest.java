package com.cengha.divider2.service;

import com.cengha.divider2.exception.UserNameAlreadyTakenException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.model.User;
import com.cengha.divider2.repository.MoveRepository;
import com.cengha.divider2.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    public UserRepository userRepository;

    public UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserService(userRepository);
    }

    @Test
    public void createTestUserNameAlreadyTakenException() {
        Mockito.when(userRepository.getFirstByUsernameAndEnabledIsTrue("username")).thenReturn(Optional.of(new User()));
        expectedException.expect(UserNameAlreadyTakenException.class);
        userService.create("username");
    }
}

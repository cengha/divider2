package com.cengha.divider2.service;

import com.cengha.divider2.exception.UserNameAlreadyTakenException;
import com.cengha.divider2.model.User;
import com.cengha.divider2.model.message.GameMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FlowServiceTest {


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    public GameService gameService;

    @MockBean
    public UserService userService;

    @MockBean
    public MoveService moveService;

    @MockBean
    public GameMessageService gameMessageService;

    public FlowService flowService;

    @Before
    public void setUp() throws Exception {
        flowService = new FlowService(userService, gameService, moveService, gameMessageService);

        User user = new User();
        user.setUsername("username");
        Mockito.when(userService.create("username")).thenReturn(user);
        Mockito.when(userService.create("username2")).thenReturn(null);
        Mockito.when(userService.save(user)).thenReturn(user);

        GameMessage gameMessage = new GameMessage();
        gameMessage.setGameId(1l);
        Mockito.when(gameMessageService.save(gameMessage)).thenReturn(gameMessage);
    }

    @Test
    public void UserNameAlreadyTakenExceptionTest() {
        expectedException.expect(UserNameAlreadyTakenException.class);
        GameMessage found = flowService.joinFirstAvailableGameOrCreateOne("username");

    }

}

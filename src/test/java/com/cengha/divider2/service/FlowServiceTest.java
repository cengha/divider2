package com.cengha.divider2.service;

import com.cengha.divider2.exception.UserNameAlreadyTakenException;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.service.impl.*;
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

    public FlowServiceImpl flowService;

    @Before
    public void setUp() throws Exception {
        flowService = new FlowServiceImpl(userService, gameService, moveService, gameMessageService);

        Mockito.when(userService.create("username")).thenThrow(UserNameAlreadyTakenException.class);

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

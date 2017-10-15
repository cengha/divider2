package com.cengha.divider2.service;

import com.cengha.divider2.exception.NoSuchGameException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.repository.GameMessageRepository;
import com.cengha.divider2.repository.GameRepository;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;

@RunWith(SpringRunner.class)
public class GameMessageServiceTest {

    @MockBean
    public GameMessageRepository repository;

    public GameMessageService gameMessageService;

    @Before
    public void setUp() throws Exception {
        gameMessageService = new GameMessageService(repository);
    }

    @Test
    public void gcreateGameMessageTest() {
        Game mock = new Game(1l);
        mock.setStarted(LocalDateTime.now());

        GameMessage mokGameMessage = new GameMessage();
        Mockito.when(repository.save(any(GameMessage.class))).thenReturn(mokGameMessage);

        GameMessage gameMessage = gameMessageService.createGameMessage(mock);

        assertTrue(gameMessage.getGameState().equals(GameState.STARTED));
    }

    @Test
    public void createTerminateGameMessageTest() {
        Game mock = new Game(1l);
        mock.setFinished(LocalDateTime.now().minusMinutes(1));

        GameMessage mokGameMessage = new GameMessage();
        Mockito.when(repository.save(any(GameMessage.class))).thenReturn(mokGameMessage);

        GameMessage gameMessage = gameMessageService.createTerminateGameMessage(mock);

        assertTrue(gameMessage.getGameState().equals(GameState.TERMINATED));
    }

}
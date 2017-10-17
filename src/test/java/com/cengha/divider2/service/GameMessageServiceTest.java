package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.repository.GameMessageRepository;
import com.cengha.divider2.service.impl.GameMessageServiceImpl;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class GameMessageServiceTest {

    @MockBean
    public GameMessageRepository repository;

    public GameMessageService gameMessageService;

    @Before
    public void setUp() throws Exception {
        gameMessageService = new GameMessageServiceImpl(repository);
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
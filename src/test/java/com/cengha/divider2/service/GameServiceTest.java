package com.cengha.divider2.service;

import com.cengha.divider2.exception.NoSuchGameException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.repository.GameRepository;
import com.cengha.divider2.service.impl.GameServiceImpl;
import com.cengha.divider2.service.impl.MoveServiceImpl;
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
import static org.mockito.Matchers.anyInt;

@RunWith(SpringRunner.class)
public class GameServiceTest {


    @Value("${divider.number.upper:1000}")
    private Integer STARTER_NUMBER_BOUND;

    private Random random = new Random();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    public GameRepository gameRepository;

    public GameServiceImpl gameService;

    @MockBean
    public MoveServiceImpl moveService;

    @Before
    public void setUp() throws Exception {
        gameService = new GameServiceImpl(gameRepository, moveService);
        ReflectionTestUtils.setField(gameService, "STARTER_NUMBER_BOUND", STARTER_NUMBER_BOUND);
    }

    @Test
    public void getByIdTest() {
        Game game = new Game(1l);
        Mockito.when(gameRepository.getById(1l)).thenReturn(Optional.of(game));
        Game found = gameService.getById(1l);
        assertEquals(found.getPlayerOneId(), new Long(1));
    }

    @Test
    public void getByIdNoSuchGameExceptionTest() {
        Mockito.when(gameRepository.getById(2l)).thenReturn(Optional.empty());
        expectedException.expect(NoSuchGameException.class);
        gameService.getById(2l);

    }

    @Test
    public void createOrUpdateGameFirstPlayerTest() {

        Mockito.when(gameRepository.getFirstByFinishedIsNullAndStartedIsNull()).thenReturn(Optional.empty());

        Game game = gameService.createOrUpdateGame(1l);
        assertEquals(game.getPlayerOneId(), new Long(1));
        assertNull(game.getStarted());
        assertNull(game.getFinished());
        assertNull(game.getPlayerTwoId());
        assertNull(game.getWinnerPlayerId());
        assertNull(game.getLastMove());
        assertNotNull(game.getCreated());

    }

    @Test
    public void createOrUpdateGameSecondPlayerTest() {

        Game gameMock = new Game(1l);
        gameMock.setId(1l);
        gameMock.setCreated(LocalDateTime.now().minusMinutes(1));

        Move move = new Move(1l, 2l, anyInt());
        Mockito.when(moveService.save(move)).thenReturn(move);
        Mockito.when(gameRepository.getFirstByFinishedIsNullAndStartedIsNull()).thenReturn(Optional.of(gameMock));

        Game game = gameService.createOrUpdateGame(2l);
        assertEquals(game.getPlayerOneId(), new Long(1));
        assertEquals(game.getPlayerTwoId(), new Long(2));
        assertNotNull(game.getCreated());
        assertNotNull(game.getStarted());
        assertNull(game.getFinished());
        assertNotNull(game.getLastMove());
        assertEquals(game.getMoves().size(), 1);
        assertTrue(random.nextInt(STARTER_NUMBER_BOUND) < 1000);

    }

    @Test
    public void retrieveGameTest() {

        Game mock = new Game(1l);
        Mockito.when(gameRepository.getById(1l)).thenReturn(Optional.of(mock));

        List<Move> moves = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            moves.add(new Move(1l, 1l, i));
        }
        Mockito.when(moveService.getAllByGameId(1l)).thenReturn(moves);

        Game game = gameService.retrieveGame(1l);
        assertTrue(game.getLastMove().getNumber().equals(moves.get(moves.size() - 1).getNumber()));


    }

    @Test
    public void updateGameIfFinishedTest() {

        Game mock = new Game(1l);
        mock.setId(1l);
        Mockito.when(gameRepository.getById(1l)).thenReturn(Optional.of(mock));


        Game game = gameService.updateGameIfFinished(mock, true, 1l);
        assertNotNull(game.getFinished());
        assertNotNull(game.getWinnerPlayerId());
        assertEquals(game.getWinnerPlayerId(), new Long(1));


    }
}

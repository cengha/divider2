package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.repository.MoveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class MoveServiceTest {


    @MockBean
    public MoveRepository moveRepository;

    public MoveService moveService;

    public List<Move> moves;

    public Game game;

    @Before
    public void setUp() throws Exception {
        moveService = new MoveService(moveRepository);

        game = new Game(1l);
        game.setId(1l);
        Optional<Game> optional = Optional.of(game);
        moves = new LinkedList<>();
        moves.add(new Move(game.getId(), 1l, 1));
        Mockito.when(moveRepository.getAllByGameId(game.getId())).thenReturn(moves);
    }

    @Test
    public void getAllByGameIdTest() {
        List<Move> found = moveService.getAllByGameId(1l);

        assertEquals(found.size(), moves.size());
    }
}

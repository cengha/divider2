package com.cengha.divider2.service.impl;

import com.cengha.divider2.exception.GameFinishedException;
import com.cengha.divider2.exception.GameNotStartedException;
import com.cengha.divider2.exception.NoSuchGameException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.repository.GameRepository;
import com.cengha.divider2.service.GameService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final MoveServiceImpl moveService;
    private Random random = new Random();

    @Value("${divider.number.upper:1000}")
    private Integer STARTER_NUMBER_BOUND;

    @Autowired
    public GameServiceImpl(GameRepository repository, MoveServiceImpl moveService) {
        this.repository = repository;
        this.moveService = moveService;
    }

    public Game getById(Long id) {
        return repository.getById(id).orElseThrow(NoSuchGameException::new);

    }

    public Game createOrUpdateGame(Long playerId) {
        Game game;
        Optional<Game> optGame = repository.getFirstByFinishedIsNullAndStartedIsNull();
        if (optGame.isPresent()) {
            game = optGame.get();
            List<Move> allMoves = moveService.getAllByGameId(game.getId());
            Move move = moveService.save(new Move(game.getId(), playerId, random.nextInt(STARTER_NUMBER_BOUND)));
            allMoves.add(move);
            game.setLastMove(move);
            game.setMoves(allMoves);
            game.setPlayerTwoId(playerId);
            game.setStarted(LocalDateTime.now());
        } else {
            game = new Game(playerId);
            game.setCreated(LocalDateTime.now());
        }
        repository.save(game);
        return game;
    }

    public Game retrieveGame(Long gameId) {
        Game game = this.getById(gameId);
        List<Move> allMovesByGameId = moveService.getAllByGameId(gameId);
        game.setMoves(allMovesByGameId);
        if (allMovesByGameId.size() > 0) {
            game.setLastMove(allMovesByGameId.get(allMovesByGameId.size() - 1));
        }
        return game;
    }

    public void validateGameState(Game game) {

        checkIfGameStarted(game);

        checkIfGameNotFinished(game);
    }

    public Game updateGameIfFinished(Long gameId, Boolean finished, Long playerId) {
        Game game = this.getById(gameId);
        if (finished) {
            updateGameAsFinished(game, playerId.equals(game.getPlayerOneId()) ? game.getPlayerTwoId() : game.getPlayerOneId());
        }
        return game;
    }

    public Game updateGameIfFinished(Game game, Boolean finished, Long winnerPlayerId) {
        if (finished) {
            updateGameAsFinished(game, winnerPlayerId);
        }
        return game;
    }

    private void updateGameAsFinished(Game game, Long winnerPlayerId) {
        game.setFinished(LocalDateTime.now());
        game.setWinnerPlayerId(winnerPlayerId);
        repository.save(game);
    }

    private void checkIfGameStarted(Game game) {
        if (game.getStarted() == null) {
            throw new GameNotStartedException();
        }
    }

    private void checkIfGameNotFinished(Game game) {
        if (game.getFinished() != null) {
            throw new GameFinishedException();
        }
    }
}

package com.cengha.divider2.service;

import com.cengha.divider2.exception.GameFinishedException;
import com.cengha.divider2.exception.GameNotStartedException;
import com.cengha.divider2.exception.NoSuchGameException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.repository.GameRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameService {

    private final GameRepository repo;
    private final MoveService moveService;
    private Random random = new Random();

    @Value("${divider.number.upper:1000}")
    private Integer STARTER_NUMBER_BOUND;

    @Autowired
    public GameService(GameRepository repo, MoveService moveService) {
        this.repo = repo;
        this.moveService = moveService;
    }

    public Game getById(Long id) {
        return repo.getById(id).orElseThrow(NoSuchGameException::new);

    }

    public Game createOrUpdateGame(Long playerId) {
        Game game;
        Optional<Game> optGame = repo.getFirstByFinishedIsNullAndStartedIsNull();
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
        repo.save(game);
        return game;
    }

    public Game retrieveGame(Long gameId) {
        Game game = this.getById(gameId);
        List<Move> allMovesByGameId = moveService.getAllByGameId(gameId);
        game.setMoves(allMovesByGameId);
        game.setLastMove(allMovesByGameId.get(allMovesByGameId.size() - 1));
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
        repo.save(game);
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

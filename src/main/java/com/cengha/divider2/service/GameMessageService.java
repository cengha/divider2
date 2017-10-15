package com.cengha.divider2.service;

import com.cengha.divider2.exception.NoSuchGameMessageException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.repository.GameMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameMessageService {

    private final GameMessageRepository repo;

    @Autowired
    public GameMessageService(GameMessageRepository repo) {
        this.repo = repo;
    }

    public GameMessage save(GameMessage gameMessage) {
        return repo.save(gameMessage);
    }

    public GameMessage createGameMessage(Game game) {
        GameMessage gameMessage = retrieveGameMessage(game);
        GameState gameState = retrieveGameState(game);
        gameMessage.setGameStateId(gameState.getStateId());
        gameMessage = repo.save(gameMessage);
        gameMessage.setGameState(gameState);
        return gameMessage;
    }

    public GameMessage createTerminateGameMessage(Game game) {
        GameMessage gameMessage = retrieveGameMessage(game);
        gameMessage.setGameStateId(3);
        gameMessage = repo.save(gameMessage);
        gameMessage.setGameState(GameState.TERMINATED);
        return gameMessage;
    }

    private GameMessage retrieveGameMessage(Game game) {
        GameMessage gameMessage = new GameMessage();
        gameMessage.setGameId(game.getId());
        gameMessage.setGame(game);
        gameMessage.setTurnPlayerId(retrieveTurnPlayerId(game, game.getLastMove()));
        gameMessage.setWinnerPlayerId(game.getWinnerPlayerId());
        return gameMessage;
    }

    private Long retrieveTurnPlayerId(Game game, Move lastMove) {
        Long turnPlayerId = game.getPlayerOneId();
        if (lastMove != null) {
            turnPlayerId = lastMove.getPlayerId().equals(game.getPlayerOneId()) ? game.getPlayerTwoId() : game.getPlayerOneId();
        }
        return turnPlayerId;
    }

    private GameState retrieveGameState(Game game) {
        return game.getFinished() != null ? GameState.FINISHED : game.getStarted() != null ? GameState.STARTED : GameState.CREATED;
    }
}

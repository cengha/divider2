package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;

public interface GameService {

    Game getById(Long id);

    Game createOrUpdateGame(Long playerId);

    Game retrieveGame(Long gameId);

    void validateGameState(Game game);

    Game updateGameIfFinished(Long gameId, Boolean finished, Long playerId);

    Game updateGameIfFinished(Game game, Boolean finished, Long winnerPlayerId);

}

package com.cengha.divider2.service;

import com.cengha.divider2.exception.NoSuchGameException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

public interface GameService {

    Game getById(Long id);

    Game createOrUpdateGame(Long playerId);

    Game retrieveGame(Long gameId);

    void validateGameState(Game game);

}

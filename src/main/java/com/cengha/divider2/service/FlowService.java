package com.cengha.divider2.service;

import com.cengha.divider2.model.message.GameMessage;

public interface FlowService {

    GameMessage joinFirstAvailableGameOrCreateOne(String username);

    GameMessage makeMove(Long gameId, Long playerId, Integer number);

    GameMessage terminateGame(Long playerId, Long gameId);

}

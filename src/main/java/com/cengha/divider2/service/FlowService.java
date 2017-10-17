package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.model.User;
import com.cengha.divider2.model.message.GameMessage;
import org.springframework.transaction.annotation.Transactional;

public interface FlowService {

    GameMessage joinFirstAvailableGameOrCreateOne(String username);

    GameMessage makeMove(Long gameId, Long playerId, Integer number);

    GameMessage terminateGame(Long playerId, Long gameId);

}

package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;

public interface GameMessageService {

    GameMessage save(GameMessage gameMessage);

    GameMessage createGameMessage(Game game);

    GameMessage createTerminateGameMessage(Game game);

}

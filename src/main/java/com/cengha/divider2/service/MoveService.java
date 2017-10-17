package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;

import java.util.List;

public interface MoveService {

    Move save(Move move);

    List<Move> getAllByGameId(Long gameId);

    void checkIfNumberValid(Integer newMoveNumber, Integer lastMoveNumber);

    void checkIfCorrectTurn(Long playerId, Long lasMovePlayerId);

    Move makeMove(Long gameId, Long playerId, Integer number, Game game);

}

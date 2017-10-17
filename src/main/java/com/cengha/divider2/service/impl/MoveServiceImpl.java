package com.cengha.divider2.service.impl;

import com.cengha.divider2.exception.IllegalNumberException;
import com.cengha.divider2.exception.NotYourTurnException;
import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.repository.MoveRepository;
import com.cengha.divider2.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveServiceImpl implements MoveService{

    private final MoveRepository repo;

    @Autowired
    public MoveServiceImpl(MoveRepository repo) {
        this.repo = repo;
    }

    public Move save(Move move) {
        return repo.save(move);
    }

    public List<Move> getAllByGameId(Long gameId){
        return repo.getAllByGameId(gameId);
    }

    public void checkIfNumberValid(Integer newMoveNumber, Integer lastMoveNumber) {
        Integer plusOne = lastMoveNumber + 1;
        Integer minusOne = lastMoveNumber - 1;
        if (newMoveNumber % 3 != 0 || !(newMoveNumber.equals(lastMoveNumber) || newMoveNumber.equals(plusOne) || newMoveNumber.equals(minusOne))) {
            throw new IllegalNumberException();
        }
    }

    public void checkIfCorrectTurn(Long playerId, Long lasMovePlayerId) {
        if (lasMovePlayerId.equals(playerId)) {
            throw new NotYourTurnException();
        }
    }

    public Move makeMove(Long gameId, Long playerId, Integer number, Game game) {
        this.checkIfCorrectTurn(playerId, game.getLastMove().getPlayerId());
        this.checkIfNumberValid(number, game.getLastMove().getNumber());
        Move move = new Move(gameId, playerId, number);
        move.setNumber(move.getNumber() / 3);
        move = this.save(move);
        return move;
    }

}

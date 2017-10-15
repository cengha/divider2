package com.cengha.divider2.service;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.model.User;
import com.cengha.divider2.model.message.GameMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlowService {

    private final UserService userService;
    private final GameService gameService;
    private final MoveService moveService;
    private final GameMessageService gameMessageService;


    @Autowired
    public FlowService(UserService userService, GameService gameService, MoveService moveService, GameMessageService gameMessageService) {
        this.userService = userService;
        this.gameService = gameService;
        this.moveService = moveService;
        this.gameMessageService = gameMessageService;
    }

    @Transactional
    public GameMessage joinFirstAvailableGameOrCreateOne(String username) {

        User user = userService.create(username);

        Game game = gameService.createOrUpdateGame(user.getId());

        return gameMessageService.createGameMessage(game);

    }

    @Transactional
    public GameMessage makeMove(Long gameId, Long playerId, Integer number) {

        Game game = gameService.retrieveGame(gameId);

        gameService.validateGameState(game);

        Move move = moveService.makeMove(gameId, playerId, number, game);

        game.setLastMove(move);

        game = gameService.updateGameIfFinished(game, move.getNumber().equals(1), playerId);

        if (game.getFinished() != null) {
            userService.updateUserAsDisabled(game.getPlayerOneId());
            userService.updateUserAsDisabled(game.getPlayerTwoId());
        }

        return gameMessageService.createGameMessage(game);

    }

    @Transactional
    public GameMessage terminateGame(Long playerId, Long gameId) {

        userService.updateUserAsDisabled(playerId);

        Game game = gameService.updateGameIfFinished(gameId, true, playerId);

        return gameMessageService.createTerminateGameMessage(game);
    }


}

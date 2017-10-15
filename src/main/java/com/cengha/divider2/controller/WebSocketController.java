package com.cengha.divider2.controller;

import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final FlowService flowService;
    private final String GAME_CHANNEL = "/ws/channel/game/";
    private final String PLAYER_CHANNEL = "/ws/channel/game/player/";
    private final String MSG_CONNECTED = "Connected";
    private final String MSG_STARTED = "Started";
    private final String MSG_TERMINATED = "Started";
    private final String MSG_FINISHED = "Finished";
    private final String MESSAGE_MAPPING_JOIN_GAME = "/game/join/{username}";
    private final String MESSAGE_MAPPING_TERMIN_GAME = "/game/{gameId}/player/{playerId}/termin";
    private final String MESSAGE_MAPPING_MAKE_MOVE = "/game/{gameId}/player/{playerId}/move/{number}";

    @Autowired
    public WebSocketController(SimpMessageSendingOperations messagingTemplate, FlowService flowService) {
        this.messagingTemplate = messagingTemplate;
        this.flowService = flowService;
    }

    @MessageMapping(MESSAGE_MAPPING_JOIN_GAME)
    public void joinGame(@DestinationVariable String username) {

        convertAndSendGameJoinMessage(username, flowService.joinFirstAvailableGameOrCreateOne(username));

    }

    @MessageMapping(MESSAGE_MAPPING_MAKE_MOVE)
    public void makeMove(@DestinationVariable Long gameId,
                         @DestinationVariable Long playerId,
                         @DestinationVariable Integer number) {

        convertAndSendMakeMove(flowService.makeMove(gameId, playerId, number));

    }

    @MessageMapping(MESSAGE_MAPPING_TERMIN_GAME)
    public void terminGame(@DestinationVariable Long gameId,
                           @DestinationVariable Long playerId) {

        convertAndSendGameTermin(flowService.terminateGame(gameId, playerId));

    }

    private void convertAndSendGameJoinMessage(String username, GameMessage gameMessage) {
        if (gameMessage.getGameState() == GameState.CREATED) {
            gameMessage.setMessage(MSG_CONNECTED);
            convertAndSendPlayerChannel(username, gameMessage);
        } else {
            gameMessage.setMessage(MSG_STARTED);
            convertAndSendPlayerChannel(username, gameMessage);
            convertAndSendToGameChannel(gameMessage);
        }
    }

    private void convertAndSendMakeMove(GameMessage gameMessage) {
        if (gameMessage.getGameState() == GameState.FINISHED) {
            gameMessage.setMessage(MSG_FINISHED);
            convertAndSendToGameChannel(gameMessage);
        } else {
            gameMessage.setMessage(MSG_STARTED);
            convertAndSendToGameChannel(gameMessage);
        }
    }

    private void convertAndSendGameTermin(GameMessage gameMessage) {
        gameMessage.setMessage(MSG_TERMINATED);
        convertAndSendToGameChannel(gameMessage);
    }

    private void convertAndSendToGameChannel(GameMessage gameMessage) {
        messagingTemplate.convertAndSend(GAME_CHANNEL + gameMessage.getGameId(), gameMessage);
    }

    private void convertAndSendPlayerChannel(String username, GameMessage gameMessage) {
        messagingTemplate.convertAndSend(PLAYER_CHANNEL + username, gameMessage);
    }


}
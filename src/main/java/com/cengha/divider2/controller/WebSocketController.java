package com.cengha.divider2.controller;

import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.service.impl.FlowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final FlowServiceImpl flowService;
    private final String GAME_CHANNEL = "/ws/channel/game/";
    private final String PLAYER_CHANNEL = "/ws/channel/game/player/";
    private final String ERROR_CHANNEL = "/ws/channel/game/player/error/";
    private final String MSG_CONNECTED = "Connected to the game";
    private final String MSG_STARTED = "You have an opponent, the game has started";
    private final String MSG_FINISHED = "Game Finished";
    private final String MSG_TERMINATED = "Game Terminated";
    private final String MSG_ERROR = "An error occured: ";
    private final String MESSAGE_MAPPING_JOIN_GAME = "/game/join/{username}";
    private final String MESSAGE_MAPPING_TERMIN_GAME = "/game/{gameId}/player/{playerId}/termin";
    private final String MESSAGE_MAPPING_MAKE_MOVE = "/game/{gameId}/player/{playerId}/move/{number}";

    @Autowired
    public WebSocketController(SimpMessageSendingOperations messagingTemplate, FlowServiceImpl flowService) {
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

    @MessageExceptionHandler
    public void handleException(Exception ex) {
        SimpMessageHeaderAccessor headerAccessor = StompHeaderAccessor.create();
        String sessionId = headerAccessor.getSessionId();
        MessageHeaders headers = createHeaders(sessionId);
        messagingTemplate.convertAndSendToUser(sessionId, ERROR_CHANNEL, MSG_ERROR + ex.getMessage(), createHeaders(sessionId));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
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

    private void convertAndSendToErrorChannel(String username) {
        messagingTemplate.convertAndSend(ERROR_CHANNEL + username);
    }

    private void convertAndSendPlayerChannel(String username, GameMessage gameMessage) {
        messagingTemplate.convertAndSend(PLAYER_CHANNEL + username, gameMessage);
    }


}
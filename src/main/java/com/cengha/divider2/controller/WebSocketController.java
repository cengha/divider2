package com.cengha.divider2.controller;

import com.cengha.divider2.model.enums.GameState;
import com.cengha.divider2.model.message.GameMessage;
import com.cengha.divider2.service.impl.FlowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final FlowServiceImpl flowService;

    @Value("${ws.channel.game}")
    private String GAME_CHANNEL;

    @Value("${ws.channel.game.player}")
    private String GAME_PLAYER_CHANNEL;

    @Value("${ws.channel.game.player.error}")
    private String GAME_PLAYER_ERROR_CHANNEL;

    @Value("${ws.msg.connected}")
    private String MSG_CONNECTED = "Connected to the game.";

    @Value("${ws.msg.started}")
    private String MSG_STARTED = "You have an opponent, the game has started.";

    @Value("${ws.msg.finished}")
    private String MSG_FINISHED = "Game Finished!";

    @Value("${ws.msg.terminated}")
    private String MSG_TERMINATED = "Game Terminated!";

    @Value("${ws.msg.error}")
    private String MSG_ERROR = "An error occurred: ";

    private final String MESSAGE_MAPPING_JOIN_GAME = "/game/join/{username}";
    private final String MESSAGE_MAPPING_TERMIN_GAME = "/game/{gameId}/player/{playerId}/termin";
    private final String MESSAGE_MAPPING_MAKE_MOVE = "/game/{gameId}/player/{playerId}/move/{number}";

    @Autowired
    public WebSocketController(SimpMessageSendingOperations messagingTemplate, FlowServiceImpl flowService) {
        this.messagingTemplate = messagingTemplate;
        this.flowService = flowService;
    }

    @MessageMapping(MESSAGE_MAPPING_JOIN_GAME)
    public void joinGame(@DestinationVariable String username,
                         SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();
        MessageHeaders headers = createHeaders(sessionId);
        convertAndSendGameJoinMessage(sessionId, headers, flowService.joinFirstAvailableGameOrCreateOne(username));

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
    private void handleException(Exception ex, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        MessageHeaders headers = createHeaders(sessionId);
        messagingTemplate.convertAndSendToUser(sessionId, GAME_PLAYER_ERROR_CHANNEL, MSG_ERROR + ex.getMessage(), headers);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    private void convertAndSendGameJoinMessage(String sessionId, MessageHeaders headers, GameMessage gameMessage) {
        if (gameMessage.getGameState() == GameState.CREATED) {
            gameMessage.setMessage(MSG_CONNECTED);
            messagingTemplate.convertAndSendToUser(sessionId, GAME_PLAYER_CHANNEL, gameMessage, headers);
        } else {
            gameMessage.setMessage(MSG_STARTED);
            messagingTemplate.convertAndSendToUser(sessionId, GAME_PLAYER_CHANNEL, gameMessage, headers);
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
}
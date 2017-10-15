package com.cengha.divider2.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketControllerTest {

    private int port = 7091;
    private String URL;


    private final String GAME_CHANNEL = "/ws/channel/game/";
    private final String PLAYER_CHANNEL = "/ws/channel/game/player/";
    private final String MESSAGE_MAPPING_JOIN_GAME = "/ws/divider/game/join/";
    private final String MESSAGE_MAPPING_TERMIN_GAME = "/ws/divider/game/" + 1 + "/player/" + "user1" + "/termin";
    private final String MESSAGE_MAPPING_MAKE_MOVE = "/ws/divider/game/" + 1 + "/player/" + "user1" + "/move/" + 27;

    StompSession stompSession;

    @Before
    public void setup() throws InterruptedException, ExecutionException, TimeoutException {
        URL = "ws://localhost:" + port + "/dws";
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompSession = stompClient.connect(URL, new TestStompSessionHandler() {
        }).get(1, SECONDS);
        stompSession.subscribe(GAME_CHANNEL + 1, new JoinGameStompFrameHandler());
        stompSession.subscribe(PLAYER_CHANNEL + "user1", new JoinGameStompFrameHandler());
    }

    @Test
    public void connectsToSocket() throws Exception {

        assertThat(stompSession.isConnected()).isTrue();
    }

    @Test
    public void testJoinGameEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        stompSession.send(MESSAGE_MAPPING_JOIN_GAME + "user1", null);
    }
    @Test
    public void testMakeMoveEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        stompSession.send(MESSAGE_MAPPING_MAKE_MOVE + "user1", null);
    }
    @Test
    public void testTerminGameEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        stompSession.send(MESSAGE_MAPPING_TERMIN_GAME + "user1", null);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class JoinGameStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Message.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            Message msg = (Message) o;
            System.out.println("Received : " + msg);
        }


    }

    private class TestStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            super.afterConnected(session, connectedHeaders);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            super.handleException(session, command, headers, payload, exception);
        }
    }
}

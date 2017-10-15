package com.cengha.divider2.model.message;

import com.cengha.divider2.model.Game;
import com.cengha.divider2.model.enums.GameState;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "game_message")
public class GameMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long gameId;

    @Transient
    private Game game;
    private String message;
    private Long turnPlayerId;
    private Long winnerPlayerId;
    private LocalDateTime created=LocalDateTime.now();
    @Transient
    private GameState gameState= GameState.CREATED;
    private Integer gameStateId=0;

}

package com.cengha.divider2.model;

import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "game")
public class Game{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long playerOneId;
    private Long playerTwoId;
    private Long winnerPlayerId;
    private LocalDateTime started;
    private LocalDateTime created=LocalDateTime.now();
    private LocalDateTime finished;

    @Transient
    private Move lastMove;

    @Transient
    private List<Move> moves;

    public Game(){ }
    public Game(Long playerOneId){
        this.playerOneId=playerOneId;
    }


}

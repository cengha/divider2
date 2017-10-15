package com.cengha.divider2.model;

import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Data
@Entity
@Table(name = "move")
public class Move implements Comparable<Move>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long gameId;
    private Long playerId;
    private Integer number;
    private LocalDateTime created=LocalDateTime.now();

    public Move() {
    }

    public Move(Long gameId, Long playerId, Integer number) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.number = number;
    }

    @Override
    public int compareTo(Move o) {
        return this.getCreated().compareTo(o.getCreated());
    }


}

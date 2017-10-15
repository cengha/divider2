package com.cengha.divider2.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by TRKoseCe on 20.07.2017.
 */

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private Boolean enabled = true;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime disconnected;


}

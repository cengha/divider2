package com.cengha.divider2.repository;

import com.cengha.divider2.model.message.GameMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameMessageRepository extends CrudRepository<GameMessage,String> {

    public Optional<GameMessage> getFirstByGameIdOrderByCreatedDesc(Long gameId);

}

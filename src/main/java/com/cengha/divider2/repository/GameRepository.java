package com.cengha.divider2.repository;

import com.cengha.divider2.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {

    Optional<Game> getFirstByFinishedIsNullAndStartedIsNull();
    Optional<Game> getById(Long id);

}

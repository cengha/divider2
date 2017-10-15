package com.cengha.divider2.repository;

import com.cengha.divider2.model.Move;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends CrudRepository<Move,String> {

    List<Move> getAllByGameId(Long gameId);
}

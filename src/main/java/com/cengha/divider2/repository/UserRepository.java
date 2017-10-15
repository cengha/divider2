package com.cengha.divider2.repository;

import com.cengha.divider2.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> getFirstByUsernameAndEnabledIsTrue(String username);

}

package com.cengha.divider2.repository;

import com.cengha.divider2.exception.UserNameAlreadyTakenException;
import com.cengha.divider2.model.Move;
import com.cengha.divider2.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() throws Exception {

        user1 = new User();
        user1.setUsername("cengha");
        testEntityManager.persist(user1);
        testEntityManager.flush();
    }


    @Test
    public void getFirstByFinishedIsNullTest() throws Exception {
        Optional<User> cengha = userRepository.getFirstByUsernameAndEnabledIsTrue("cengha");
        assertTrue(cengha.isPresent());
    }
}

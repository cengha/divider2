package com.cengha.divider2.repository;

import com.cengha.divider2.model.Move;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MoveRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MoveRepository moveRepository;

    private Move move1;
    private Move move2;
    private Move move3;

    @Before
    public void setUp() throws Exception {

        move1 = new Move(1l, 1l, 6);
        testEntityManager.persist(move1);

        move2 = new Move(1l, 12l, 6);
        testEntityManager.persist(move2);

        move3 = new Move(2l, 12l, 6);
        testEntityManager.persist(move3);

        testEntityManager.flush();
    }


    @Test
    public void getFirstByFinishedIsNullTest() throws Exception {
        List<Move> found = moveRepository.getAllByGameId(1l);
        assertTrue(found.contains(move1));
        assertTrue(found.contains(move2));
        assertTrue(!found.contains(move3));
        for (Move temp : found) {
            assertNotNull(temp.getCreated());
        }
    }
}

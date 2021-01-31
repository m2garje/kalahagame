package com.backbase.game.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.backbase.game.model.Game;
import com.backbase.game.model.GameResponse;

/**
 *
 * @author Mahesh G
 *
 */

@SpringBootTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void shouldCreateGame() {

        // given
        GameResponse gameInfoResponse = gameRepository.create(6);

        // when
        Game game = gameRepository.findById(gameInfoResponse.getId());

        // assert
        Assertions.assertNotNull(game);
        Assertions.assertEquals(gameInfoResponse, gameInfoResponse);
    }
}

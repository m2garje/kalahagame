package com.backbase.game.repository;

import com.backbase.game.model.Game;
import com.backbase.game.model.GameResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * @author Mahesh G
 */

@ExtendWith(MockitoExtension.class)
public class GameRepositoryTest {

    @InjectMocks
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

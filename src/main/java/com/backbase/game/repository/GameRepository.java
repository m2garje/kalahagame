package com.backbase.game.repository;

import com.backbase.game.controller.GameController;
import com.backbase.game.exception.GameException;
import com.backbase.game.model.Game;
import com.backbase.game.model.GameResponse;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is responsible for create and retrieve a Game by using In-Memory games store.
 *
 * @author Mahesh G
 */
@Repository
public class GameRepository {

    private static final Map<String, Game> games = new ConcurrentHashMap<>();

    /**
     * This method create new Game and save the object in a Map.
     *
     * @param initialStonesCountPerPit is the number of the stone of a pit.
     * @return GameResponseInfo object.
     */
    public GameResponse create(Integer initialStonesCountPerPit) {
        String id = UUID.randomUUID().toString();
        Game game = new Game(initialStonesCountPerPit);
        game.setId(id);
        games.put(id, game);

        String gameLink = linkTo(methodOn(GameController.class).createAGameAndFillDefaultStones()).slash(id).toString();
        return GameResponse.builder().id(id).url(gameLink).build();
    }

    /**
     * This method will return the game object by id.
     *
     * @param id is the game id.
     * @return Game if present.
     */
    public Game findById(String id) {
        Game game = games.get(id);
        if (game == null) {
            throw new GameException("Game is not present for the id: " + id);
        }
        return game;
    }

}

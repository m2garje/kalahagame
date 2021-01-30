package com.backbase.game.service;
import java.util.Map;
import java.util.TreeMap;

import com.backbase.game.KalahGameApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.backbase.game.controller.GameController;
import com.backbase.game.model.GameResponse;
import com.backbase.game.model.Pit;
import com.backbase.game.evaluate.EvaluateGameFlow;
import com.backbase.game.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Kalah game consists of two operation.
 * 1. Initialize a game with default stones.
 * 2. Play by pick stones from current players pit(1-6 and 8-13) except their Kalah/House(7 and 14)
 *
 * @author Mahesh G
 *
 */
@Service @Slf4j
public class GameService implements Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final EvaluateGameFlow gameEngine;

    public GameService(GameRepository gameRepository, EvaluateGameFlow gameEngine) {
        this.gameRepository = gameRepository;
        this.gameEngine = gameEngine;
    }

    /**
     * This method is responsible to initialize new game
     *
     * @param initialStonesCountPerPit is the initial number of stone.
     *
     * @return GameInfo
     */
    @Override
    public GameResponse initializeAGame(Integer initialStonesCountPerPit) {
        return gameRepository.create(initialStonesCountPerPit);
    }

    /**
     * This method is responsible for every new move of the stones from a pit.
     * @param gameId game id
     * @param pitId id of the pit
     * @return GameResponseInfo which contains the status of pits.
     */
    @Override
    public GameResponse play(String gameId, Integer pitId) {
        log.debug("gameId {}, pitId {}", gameId, pitId);

        com.backbase.game.model.Game game = gameRepository.findById(gameId);
        Pit pit = game.getBoard().getPitByPitId(pitId);

         gameEngine.execute(game, pit);

        return createCurrentGameStatusResponse(gameId, game);

    }

    private GameResponse createCurrentGameStatusResponse(String gameId, com.backbase.game.model.Game game) {

        Map<String, String> statusInfo = new TreeMap<>();

        game.getBoard().getPits().entrySet().forEach(entry -> {
            statusInfo.put(Integer.toString(entry.getKey()), Integer.toString(entry.getValue().getStoneCount()));
        });

        String gameLink = linkTo(methodOn(GameController.class).createAGameAndFillDefaultStones()).slash(gameId)
                .toString();

        return GameResponse.builder().id(gameId).url(gameLink).status(statusInfo).build();
    }

}

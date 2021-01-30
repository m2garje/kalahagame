package com.backbase.game.controller;

import com.backbase.game.model.Board;
import com.backbase.game.model.GameResponse;
import com.backbase.game.service.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * This is controller class for kalah game.
 * @author Mahesh G
 *
 */
@Api(value = GameController.GAME_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = GameController.GAME_PATH, produces = { MediaType.APPLICATION_JSON_VALUE })
@RestController @Validated @Slf4j
public class GameController {

    public static final String GAME_PATH = "games";
    private final Game gameService;
    public GameController(Game gameService) {
        this.gameService = gameService;
    }

    /**
     * Create a Kalah Game and Initialize the Stones in each players pit except player's Kalah/House.
     * @return GameResponseInfo which has game id and url.
     */
    @ApiOperation(value = "Create a Kalah Game and Initialize the Stones in each players pit except player's Kalah/House",
            httpMethod = "POST",
            response = GameResponse.class)

    @PostMapping
    public ResponseEntity<GameResponse> createAGameAndFillDefaultStones() {

        log.info("Starting kalah game");

        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.initializeAGame(Board.INITIAL_STONE_ON_PIT));
    }

    /**
     * Make a move based on the given GameId, PitId.
     * Apply various game rules and update the pits accordingly.
     *
     * @param gameId current Game Identifier
     * @param pitId selected Pit Identifier
     * @return GameResponse with pits status and house status.
     */
    @ApiOperation(
            value = "Using gameId, pick stones from any pitId and make a move. Kalah game will let you know the status at the end of this move",
            httpMethod = "PUT",
            response = GameResponse.class)
    @PutMapping("/{gameId}/pits/{pitId}")
    public ResponseEntity<GameResponse> makeAMove(
            @ApiParam(name = "gameId", value = "Current Game Identifier", required = true)
            @PathVariable String gameId,
            @ApiParam(name = "pitId", value = "The selected PIT Identifier to make a move", required = true)
            @PathVariable Integer pitId) {

        log.info("Picking stones from {} for the game {}", pitId, gameId);

        return ResponseEntity.ok().body(gameService.play(gameId, pitId));
    }
}

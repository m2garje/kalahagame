package com.backbase.game.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.backbase.game.configuration.ApplicationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.backbase.game.controller.GameController;
import com.backbase.game.model.Board;
import com.backbase.game.model.GameResponse;
import com.backbase.game.model.GameStatus;
import com.backbase.game.model.Pit;
import com.backbase.game.model.Player;
import com.backbase.game.evaluate.EvaluateGameFlow;
import com.backbase.game.repository.GameRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 *
 * @author Mahesh G
 *
 */
@SpringBootTest
public class GameServiceTest {

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private EvaluateGameFlow gameEngine;

    @Autowired
    private Game gameService;

    @Autowired
    private ApplicationConfig applConfig;

    @Test
    public void shouldInitGame(){

        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(UUID.randomUUID().toString());
        gameResponse.setUrl(linkTo(methodOn(GameController.class).createAGameAndFillDefaultStones()).slash(gameResponse.getId())
                .toString());
        gameResponse.setStatus(null);

        //given
        BDDMockito.given(gameRepository.create(BDDMockito.any())).willReturn(gameResponse);

        //when
        GameResponse mockGame = gameService.initializeAGame(6);

        //then
        Assertions.assertEquals(gameResponse, mockGame);

    }

    @Test
    public void shouldPlayGame(){

        Player player1 = Player.PLAYER_1;
        Player player2 = Player.PLAYER_2;

        Board board = new Board();
        board.setPits(initPit());

        String id = UUID.randomUUID().toString();
        com.backbase.game.model.Game game = new com.backbase.game.model.Game(applConfig.getNoOfStones());
        game.setGameStatus(GameStatus.INIT);
        game.setId(id);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setBoard(board);

        //given1
        BDDMockito.given(gameRepository.findById(id)).willReturn(game);

        //given2
        Map<String, String> gameStatusCurrentValues = new TreeMap<>();
        gameStatusCurrentValues.put("1", "6");

        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(UUID.randomUUID().toString());
        gameResponse.setUrl(linkTo(methodOn(GameController.class).createAGameAndFillDefaultStones()).slash(gameResponse.getId())
                .toString());
        gameResponse.setStatus(gameStatusCurrentValues);

        BDDMockito.given(gameRepository.create(BDDMockito.any())).willReturn(gameResponse);

        //when
        GameResponse mockResponse =  gameService.play(game.getId(), game.getBoard().getPits().get(1).getPitId());

        //then
        Assertions.assertEquals(gameResponse.getStatus().isEmpty(), mockResponse.getStatus().isEmpty());
    }

    private Map<Integer, Pit> initPit(){
        Map<Integer, Pit> pits = new HashMap<>();

        pits.putAll(createPits(Board.PIT_START_ID, Board.PLAYER1_KALAH, applConfig.getNoOfStones(), Player.PLAYER_1.getPlayerId()));

        Pit house1 = new Pit(Board.PLAYER1_KALAH, Board.INITIAL_STONE_ON_KALAH, Player.PLAYER_1.getPlayerId());
        pits.put(Board.PLAYER1_KALAH, house1);

        pits.putAll(createPits(Board.PLAYER1_KALAH + 1, Board.PLAYER2_KALAH, applConfig.getNoOfStones(), Player.PLAYER_2.getPlayerId()));

        Pit house2 = new Pit(Board.PLAYER2_KALAH, Board.INITIAL_STONE_ON_KALAH, Player.PLAYER_2.getPlayerId());
        pits.put(Board.PLAYER2_KALAH, house2);

        return pits;
    }

    private Map<Integer, Pit> createPits(Integer pitStartId, Integer pitEndId, Integer initialStoneOnPit, Integer playerId) {
        return
                IntStream.range(pitStartId, pitEndId)
                        .mapToObj(pitId -> new Pit(pitId, initialStoneOnPit, playerId))
                        .collect(Collectors.toMap(Pit::getPitId, Function.identity()));
    }
}

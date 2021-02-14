package com.backbase.game.service;

import com.backbase.game.evaluate.EvaluateGameFlow;
import com.backbase.game.exception.IllegalMoveException;
import com.backbase.game.model.Board;
import com.backbase.game.model.Game;
import com.backbase.game.model.GameStatus;
import com.backbase.game.model.Pit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class GameEngineTest {

    @InjectMocks
    private EvaluateGameFlow evaluateGame;


    @Test
    public void shouldStartWithOwnPit() {

        //given
        Game game = new Game(6);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(1));

        //then
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(1));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(2));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(3));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(4));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(5));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(6));
        Assertions.assertEquals(Integer.valueOf(1), game.getBoard().getStoneCountByPitId(7));
        Assertions.assertEquals(GameStatus.PLAYER1_TURN, game.getGameStatus());
        Assertions.assertEquals(Integer.valueOf(1), game.getBoard().getPits().get(Board.PLAYER1_KALAH).getStoneCount());
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getPits().get(Board.PLAYER2_KALAH).getStoneCount());

    }

    @Test
    public void shouldNotStartWithEmptyPit() {
        //given
        Game game = new Game(6);
        Pit pit = game.getBoard().getPits().get(2);
        pit.setStoneCount(0);

        //when
        Assertions.assertThrows(IllegalMoveException.class, () -> {
            evaluateGame.execute(game, game.getBoard().getPitByPitId(2));
        });

    }

    @Test
    public void shouldNotStartWithOpponentPit() {
        //given
        Game game = new Game(6);
        game.setGameStatus(GameStatus.PLAYER2_TURN);

        //when
        Assertions.assertThrows(IllegalMoveException.class, () -> {
            evaluateGame.execute(game, game.getBoard().getPitByPitId(2));
        });
    }

    @Test
    public void shouldDistributeStoneFromPlayer2PitToPlayer1Pit() {
        //given
        Game game = new Game(6);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(12));

        //then
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(12));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(13));
        Assertions.assertEquals(Integer.valueOf(1), game.getBoard().getStoneCountByPitId(14));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(1));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(2));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(3));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(4));
        Assertions.assertEquals(GameStatus.PLAYER1_TURN, game.getGameStatus());
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getPits().get(Board.PLAYER1_KALAH).getStoneCount());
        Assertions.assertEquals(Integer.valueOf(1), game.getBoard().getPits().get(Board.PLAYER2_KALAH).getStoneCount());
    }

    @Test
    public void shouldDistributeStoneFromPlayer1PitToPlayer2Pit() {
        //given
        Game game = new Game(6);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(4));

        //then
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(4));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(5));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(6));
        Assertions.assertEquals(Integer.valueOf(1), game.getBoard().getStoneCountByPitId(7));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(8));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(9));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(10));
        Assertions.assertEquals(GameStatus.PLAYER2_TURN, game.getGameStatus());
        Assertions.assertEquals(Integer.valueOf(1), game.getBoard().getPits().get(Board.PLAYER1_KALAH).getStoneCount());
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getPits().get(Board.PLAYER2_KALAH).getStoneCount());
    }

    @Test
    public void shouldDistribute13Stone() {
        //given
        Game game = new Game(6);
        game.getBoard().getPits().get(4).setStoneCount(13);
        game.getBoard().getPits().get(10).setStoneCount(10);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(4));

        //then
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(4));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(5));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(6));
        Assertions.assertEquals(Integer.valueOf(13), game.getBoard().getStoneCountByPitId(7));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(8));
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getStoneCountByPitId(9));
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(10));
        Assertions.assertEquals(GameStatus.PLAYER2_TURN, game.getGameStatus());
        Assertions.assertEquals(Integer.valueOf(13), game.getBoard().getPits().get(Board.PLAYER1_KALAH).getStoneCount());
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getPits().get(Board.PLAYER2_KALAH).getStoneCount());
    }

    @Test
    public void shouldIncreaseHouseStoneOnOwnEmptyPit() {
        //given
        Game game = new Game(6);
        Pit pit1 = game.getBoard().getPitByPitId(1);
        pit1.setStoneCount(2);

        Pit pit2 = game.getBoard().getPitByPitId(3);
        pit2.setStoneCount(0);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(1));

        //then
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(1));
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(3));
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getStoneCountByPitId(11));
        Assertions.assertEquals(GameStatus.PLAYER2_TURN, game.getGameStatus());
        Assertions.assertEquals(Integer.valueOf(7), game.getBoard().getPits().get(Board.PLAYER1_KALAH).getStoneCount());
        Assertions.assertEquals(Integer.valueOf(0), game.getBoard().getPits().get(Board.PLAYER2_KALAH).getStoneCount());
    }


    @Test
    public void shouldChangeGameToPlayerTurn1() {
        //given
        Game game = new Game(6);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(1));

        //then
        Assertions.assertEquals(GameStatus.PLAYER1_TURN, game.getGameStatus());
    }


    @Test
    public void shouldChangeGameToPlayerTurn2() {
        //given
        Game game = new Game(6);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(2));

        //then
        Assertions.assertEquals(GameStatus.PLAYER2_TURN, game.getGameStatus());
    }


    @Test
    public void shouldChangeGameToPlayerTurn2Again() {

        //given
        Game game = new Game(6);

        //when
        Pit pit = game.getBoard().getPits().get(8);
        pit.setStoneCount(6);
        evaluateGame.execute(game, game.getBoard().getPitByPitId(8));

        //then
        Assertions.assertEquals(GameStatus.PLAYER2_TURN, game.getGameStatus());
    }


    @Test
    public void shouldGameOver() {

        //given
        Game game = new Game(6);
        for (Integer key : game.getBoard().getPits().keySet()) {
            Pit pit = game.getBoard().getPits().get(key);
            if (!pit.isKalahPit()) {
                pit.setStoneCount(0);
            }
        }

        game.getBoard().getPits().get(6).setStoneCount(1);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(6));

        //then
        Assertions.assertEquals(GameStatus.FINISHED, game.getGameStatus());
        Assertions.assertEquals(game.getWinner(), game.getPlayer1());
    }

    @Test
    public void shouldPlayer1Win() {

        //given
        Game game = new Game(6);
        for (Integer key : game.getBoard().getPits().keySet()) {
            Pit pit = game.getBoard().getPits().get(key);
            if (!pit.isKalahPit()) {
                pit.setStoneCount(0);
            }
        }
        Pit lastPit = game.getBoard().getPits().get(6);
        lastPit.setStoneCount(1);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(6));

        //then
        Assertions.assertEquals(GameStatus.FINISHED, game.getGameStatus());
        Assertions.assertEquals(game.getWinner(), game.getPlayer1());
    }

    @Test
    public void shouldPlayer2Win() {

        //given
        Game game = new Game(6);
        for (Integer key : game.getBoard().getPits().keySet()) {
            Pit pit = game.getBoard().getPits().get(key);
            if (!pit.isKalahPit()) {
                pit.setStoneCount(0);
            }
        }
        game.getBoard().getPits().get(13).setStoneCount(1);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(13));

        //then
        Assertions.assertEquals(GameStatus.FINISHED, game.getGameStatus());
        Assertions.assertEquals(game.getWinner(), game.getPlayer2());
    }

    @Test
    public void shouldDraw() {

        //given
        Game game = new Game(6);
        for (Integer key : game.getBoard().getPits().keySet()) {
            Pit pit = game.getBoard().getPits().get(key);
            if (!pit.isKalahPit()) {
                pit.setStoneCount(0);
            }
        }
        game.getBoard().getPits().get(6).setStoneCount(1);
        game.getBoard().getPits().get(14).setStoneCount(1);

        //when
        evaluateGame.execute(game, game.getBoard().getPitByPitId(6));

        //then
        Assertions.assertEquals(GameStatus.FINISHED, game.getGameStatus());
        Assertions.assertEquals(game.getWinner(), null);
    }

}



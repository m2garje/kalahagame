package com.backbase.game.evaluate;
import com.backbase.game.model.Board;
import com.backbase.game.model.Game;
import com.backbase.game.model.GameStatus;
import com.backbase.game.model.Pit;
import com.backbase.game.model.Player;
import com.backbase.game.exception.IllegalMoveException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class check starting rules for the distributing stones.
 *
 * @author Mahesh G
 */
@Slf4j
public class StartPitCondition extends GameCondition {

    @Override
    public void apply(Game game, Pit startPit) {
        log.debug("check rules for the start pit {}", startPit);
        checkPlayerTurnRule(game, startPit);
        checkEmptyStartRule(startPit);
        this.next.apply(game, startPit);
    }

    private void checkPlayerTurnRule(Game game, Pit startPit) {

        if(null==startPit){
            throw new IllegalMoveException("Please select pits from 1-6 OR 8-13");
        }

        if(startPit.getPitId()== Board.PLAYER1_KALAH || startPit.getPitId()==Board.PLAYER2_KALAH){
            throw new IllegalMoveException("Kalah or House stone(s) are not allowed to distribute.");
        }

        if (game.getGameStatus().equals(GameStatus.INIT)) {
            GameStatus gameStatus = startPit.getPlayerId().equals(Player.PLAYER_1.getPlayerId()) ? GameStatus.PLAYER1_TURN
                    : GameStatus.PLAYER2_TURN;
            game.setGameStatus(gameStatus);
        }

        if ((game.getGameStatus().equals(GameStatus.PLAYER1_TURN) && startPit.getPitId() > Board.PLAYER1_KALAH)
                ||
                (game.getGameStatus().equals(GameStatus.PLAYER2_TURN) && startPit.getPitId()<Board.PLAYER1_KALAH)) {
            throw new IllegalMoveException("Incorrect pit to play");
        }


    }

    private void checkEmptyStartRule(Pit startPit) {

        if (startPit.getStoneCount() == 0) {
            throw new IllegalMoveException("Cannot start from empty pit");
        }
    }
}

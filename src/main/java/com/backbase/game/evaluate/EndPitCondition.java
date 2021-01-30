package com.backbase.game.evaluate;

import com.backbase.game.model.Game;
import com.backbase.game.model.GameStatus;
import com.backbase.game.model.Pit;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible to check the last stone placing rules.
 *
 * @author Mahesh G
 */
@Slf4j
public class EndPitCondition extends GameCondition {

    @Override
    public void apply(Game game, Pit endPit) {
        log.debug("checking end rule for the last pit {}", endPit);

        lastEmptyPitRule(game, endPit);
        nextPlayerTurnRule(game, endPit);
        this.next.apply(game, endPit);
    }

    private void lastEmptyPitRule(Game game, Pit endPit) {

        if (!endPit.isKalahPit() && endPit.isPlayerPit(game.getGameStatus()) && endPit.getStoneCount().equals(1)) {
            Pit oppositePit = game.getBoard().getOppositePit(endPit);
            if (oppositePit.getStoneCount() > 0) {
                Pit house = game.getBoard().getPlayerKalah(endPit.getPlayerId());
                house.setStoneCount((house.getStoneCount() + oppositePit.getStoneCount()) + endPit.getStoneCount());
                oppositePit.setStoneCount(0);
                endPit.setStoneCount(0);
            }
        }
    }

    private void nextPlayerTurnRule(Game game, Pit endPit) {

        if (endPit.isPlayer1House() && game.getGameStatus().equals(GameStatus.PLAYER1_TURN)) {
            game.setGameStatus(GameStatus.PLAYER1_TURN);
        } else if (endPit.isPlayer2House() && game.getGameStatus().equals(GameStatus.PLAYER2_TURN)) {
            game.setGameStatus(GameStatus.PLAYER2_TURN);
        } else {
            GameStatus changeStage =
                    game.getGameStatus() == GameStatus.PLAYER1_TURN ? GameStatus.PLAYER2_TURN : GameStatus.PLAYER1_TURN;
            game.setGameStatus(changeStage);
        }
        game.updateTime();
    }
}

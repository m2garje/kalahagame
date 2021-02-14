package com.backbase.game.evaluate;

import com.backbase.game.model.Board;
import com.backbase.game.model.Game;
import com.backbase.game.model.GameStatus;
import com.backbase.game.model.Pit;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible to check the game end rules.
 *
 * @author Mahesh G
 */
@Slf4j
public class GameOver extends GameCondition {

    @Override
    public void apply(Game game, Pit currentPit) {
        log.debug("checking game end rule");

        Integer player1StoneCount = game.getBoard().getPlayer1PitStones();
        Integer player2StoneCount = game.getBoard().getPlayer2PitStones();

        if (player1StoneCount == 0 || player2StoneCount == 0) {

            game.setGameStatus(GameStatus.FINISHED);

            Pit house1 = game.getBoard().getPits().get(Board.PLAYER1_KALAH);
            house1.setStoneCount(house1.getStoneCount() + player1StoneCount);

            Pit house2 = game.getBoard().getPits().get(Board.PLAYER2_KALAH);
            house2.setStoneCount(house2.getStoneCount() + player2StoneCount);

            determineResult(game, house1.getStoneCount(), house2.getStoneCount());

            resetBoard(game);

        }
    }

    private void resetBoard(Game game) {

        game.getBoard().getPits().keySet()
                .stream()
                .filter(key -> !key.equals(Board.PLAYER1_KALAH) || !key.equals(Board.PLAYER2_KALAH))
                .forEach(key -> {
                    Pit pit = game.getBoard().getPits().get(key);
                    pit.setStoneCount(0);
                });

    }

    private void determineResult(Game game, Integer kalah1StoneCount, Integer kalah2StoneCount) {
        if (kalah1StoneCount > kalah2StoneCount) {
            game.setWinner(game.getPlayer1());
        } else if (kalah1StoneCount < kalah2StoneCount) {
            game.setWinner(game.getPlayer2());
        } else {
            game.setWinner(null);
        }
    }
}

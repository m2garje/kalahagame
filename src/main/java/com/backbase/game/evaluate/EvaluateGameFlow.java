package com.backbase.game.evaluate;

import org.springframework.stereotype.Component;

import com.backbase.game.model.Game;
import com.backbase.game.model.Pit;

/**
 * This class used to prepare Game evaluation conditions. Later based on Game play condition
 * will be evaluated in a sequential order.
 *
 * @author Mahesh G
 */
@Component
public class EvaluateGameFlow {

    private final GameCondition condition;

    /**
     * Evaluate the current game based on predefined set of condition order.
     */
    public EvaluateGameFlow() {

        this.condition = new StartPitCondition();
        condition.setNext(new DistributePitStoneRule())
                .setNext(new EndPitCondition())
                .setNext(new GameOver());
    }

    public void execute(Game game, Pit pit) {
        this.condition.apply(game, pit);
    }

}

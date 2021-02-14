package com.backbase.game.evaluate;

import com.backbase.game.model.Game;
import com.backbase.game.model.Pit;

/**
 * This is abstract class with one abstract method apply.
 */
public abstract class GameCondition {

    protected GameCondition next;

    public abstract void apply(Game game, Pit currentPit);

    public GameCondition setNext(GameCondition next) {
        this.next = next;
        return next;
    }

}

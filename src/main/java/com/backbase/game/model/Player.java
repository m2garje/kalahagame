package com.backbase.game.model;

/**
 * This Kalah game is supported for TWO players.
 *
 * @author Mahesh G
 */
public enum Player {

    PLAYER_1(1),
    PLAYER_2(2);

    private final Integer playerId;

    Player(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

}

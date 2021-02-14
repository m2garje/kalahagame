package com.backbase.game.service;

import com.backbase.game.model.GameResponse;

/**
 * Kalah game consists of two operation.
 * 1. Initialize a game with default stones. This value is configurable in properties file.
 * 2. Play by pick stones from current players pit(1-6 and 8-13) except their Kalah/House(7 and 14)
 *
 * @author Mahesh G
 */
public interface Game {

    GameResponse initializeAGame(Integer initialStonesCountPerPit);

    GameResponse play(String gameId, Integer pitId);

}

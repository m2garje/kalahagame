package com.backbase.game.exception;

/**
 * This exception will be thrown whenever system behaves differently.
 *
 * @author Mahesh G
 */
public class GameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GameException(String message) {
        super(message);
    }
}

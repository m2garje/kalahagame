package com.backbase.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * This class represent the game. A game contain players, board, games status
 * and updateTime time.
 *
 * @author Mahesh G
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Game {

    private String id;
    private Board board;
    private Player player1;
    private Player player2;
    private Player winner;
    private GameStatus gameStatus;
    private LocalDateTime updateAt;

    public Game(Integer initialStonesCountPerPit) {
        this.player1 = Player.PLAYER_1;
        this.player2 = Player.PLAYER_2;
        this.board = new Board(initialStonesCountPerPit, this.player1, this.player2);
        this.gameStatus = GameStatus.INIT;
        this.updateAt = LocalDateTime.now();
    }

    /**
     * This method is set the time of the last activity.
     */
    public void updateTime() {
        this.setUpdateAt(LocalDateTime.now());
    }

}

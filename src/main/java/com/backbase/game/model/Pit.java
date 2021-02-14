package com.backbase.game.model;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * This class represent the pit of the board.
 *
 * @author Mahesh G
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pit {

    @NotNull
    private Integer pitId;

    @NotNull
    private Integer stoneCount;

    @NotNull
    private Integer playerId;

    /**
     * This method is use to determine different between pit & player house.
     *
     * @return Boolean false if player1 with house2 Or player 2 with house1,
     * otherwise true
     */
    public Boolean isDistributable(GameStatus gameStatus) {

        return (!gameStatus.equals(GameStatus.PLAYER1_TURN) || !this.pitId.equals(Board.PLAYER2_KALAH))
                && (!gameStatus.equals(GameStatus.PLAYER2_TURN) || !this.pitId.equals(Board.PLAYER1_KALAH));
    }

    /**
     * This method is use to determine the ownership of current player. Player1
     * owns pit id from 1-7. Player 2 owns pit id from 8-14.
     *
     * @param gameStatus current game state. In this case player turn
     * @return True if current player is the owner of this pit otherwise false.
     */
    public Boolean isPlayerPit(GameStatus gameStatus) {

        return (gameStatus.equals(GameStatus.PLAYER1_TURN) && this.playerId.equals(Player.PLAYER_1.getPlayerId()))
                || (gameStatus.equals(GameStatus.PLAYER2_TURN) && this.playerId.equals(Player.PLAYER_2.getPlayerId()));

    }

    /**
     * This method determine that if this pit is use as Pit or as House. Pit
     * id 7 & 14 is using as House
     *
     * @return True is pit uses as house otherwise false.
     */
    public Boolean isKalahPit() {
        return this.pitId.equals(Board.PLAYER1_KALAH) || this.pitId.equals(Board.PLAYER2_KALAH);
    }

    /**
     * Find the next pit id
     *
     * @return pitId of the next Pit
     */
    public Integer nextPitIndex() {
        Integer nextPitId = this.pitId + 1;
        return nextPitId > Board.PLAYER2_KALAH ? 1 : nextPitId;
    }

    /**
     * Determine the pit as player1 house.
     *
     * @return true if player1 house
     */
    public Boolean isPlayer1House() {
        return this.playerId.equals(Player.PLAYER_1.getPlayerId()) && this.pitId.equals(Board.PLAYER1_KALAH);

    }

    /**
     * Determine the pit as player2 house.
     *
     * @return true if player2 house
     */
    public Boolean isPlayer2House() {
        return this.playerId.equals(Player.PLAYER_2.getPlayerId()) && this.pitId.equals(Board.PLAYER2_KALAH);
    }

    /**
     * This method return the opposite pit id.
     * eg. 14-5= 9 , 14-1=13 , 14-8=6
     *
     * @return pitIndex of the opposite pit.
     */
    public Integer getOppositePitId() {
        return (Board.PIT_START_ID + Board.PIT_END_ID - 1) - this.getPitId();
    }

}

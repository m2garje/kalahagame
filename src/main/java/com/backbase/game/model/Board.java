package com.backbase.game.model;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.backbase.game.exception.GameException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represent the board of the game. Board contain all the pits.
 * @author Mahesh G
 */
@ToString
@Getter @Setter
@NoArgsConstructor
public class Board {

    public static final Integer PIT_START_ID = 1;

    public static final Integer PIT_END_ID = 14;

    public static final Integer PLAYER1_KALAH = 7;

    public static final Integer PLAYER2_KALAH = 14;

    public static final Integer INITIAL_STONE_ON_PIT = 6;

    public static final Integer INITIAL_STONE_ON_KALAH = 0;

    private Map<Integer, Pit> pits;

    /**
     * Creates a Kalah Game Board by using initialStones with Players.
     *
     * @param initialStonesCountPerPit game starts with this initialStones
     * @param player1 contains information about {@link Player}
     * @param player2 contains information about {@link Player}
     */
    public Board(Integer initialStonesCountPerPit, Player player1, Player player2) {
        this.pits = initPit(initialStonesCountPerPit, player1, player2);
    }

    /**
     * Creates a Kalah game board pit's by filling initialStones (eg. 6 stones per {@link Pit})
     * @param initialStoneOnPit game starts with this initialStones
     * @param player1 contains information about {@link Player}
     * @param player2 contains information about {@link Player}
     * @return Map<Integer, Pit> contains KEY as pitId and VALUE as {@link Pit}
     */
    private Map<Integer, Pit> initPit(Integer initialStoneOnPit, Player player1, Player player2) {

        Map<Integer, Pit> pits = new ConcurrentHashMap<>();
        pits.putAll(createPit(Board.PIT_START_ID, Board.PLAYER1_KALAH, initialStoneOnPit, player1.getPlayerId()));

        Pit house1 = new Pit(Board.PLAYER1_KALAH, Board.INITIAL_STONE_ON_KALAH, player1.getPlayerId());
        pits.put(Board.PLAYER1_KALAH, house1);

        pits.putAll(createPit(Board.PLAYER1_KALAH + 1, Board.PLAYER2_KALAH, initialStoneOnPit, player2.getPlayerId()));

        Pit house2 = new Pit(Board.PLAYER2_KALAH, Board.INITIAL_STONE_ON_KALAH, player2.getPlayerId());
        pits.put(Board.PLAYER2_KALAH, house2);

        return pits;
    }

    /**
     * Create a collection of Pit using various parameter
     * @param pitStartId start pitId
     * @param pitEndId end pitId
     * @param initialStoneOnPit game with initial stones
     * @param playerId given playerId
     * @return Map<Integer, Pit> contains KEY as pitId and VALUE as {@link Pit}
     */
    private Map<Integer, Pit> createPit(Integer pitStartId, Integer pitEndId, Integer initialStoneOnPit, Integer playerId) {
        return
                IntStream.range(pitStartId, pitEndId)
                        .mapToObj(pitId -> new Pit(pitId, initialStoneOnPit, playerId))
                        .collect(Collectors.toConcurrentMap(Pit::getPitId, Function.identity()));
    }

    /**
     * To identify number of stones on this {@link Pit} by using pitId
     * @param pitId Pit id.
     * @return Integer number of total stone on a pit
     */
    public Integer getStoneCountByPitId(Integer pitId) {
        return getPitByPitId(pitId).getStoneCount();
    }

    /**
     * This utility method directly gets the {@link Player} Kalah or House {@link Pit}
     * @param playerId - default values are 7 and 14.
     * @return Pit
     */
    public Pit getPlayerKalah(Integer playerId) {
        if (playerId.equals(Player.PLAYER_1.getPlayerId())) {
            return pits.get(Board.PLAYER1_KALAH);
        } else if (playerId.equals(Player.PLAYER_2.getPlayerId())) {
            return pits.get(Board.PLAYER2_KALAH);
        }

        throw new GameException("playerId is not correct");
    }

    /**
     * This utility method gets the pit by using pitId
     *
     * @param pitId id of pit
     * @return pit found by that id.
     */
    public Pit getPitByPitId(Integer pitId) {
        return pits.get(pitId);
    }

    /**
     * This utility method gets the NEXT Pit by using given pit.
     *
     * @param pit any input.
     * @return pit with next location.
     */
    public Pit getNextPit(Pit pit) {
        return pits.get(pit.nextPitIndex());
    }

    /**
     * This utility method gets the opposite by using given Pit
     *
     * @param pit as input
     * @return pit which is opposite
     */
    public Pit getOppositePit(Pit pit) {
        return pits.get(pit.getOppositePitId());
    }

    /**
     * This Utility method counts total number of stones in player1 Pits (1-6)
     *
     * @return a positive Integer value
     */
    public Integer getPlayer1PitStones() {

        return getTotalStones(Board.PIT_START_ID, Board.PLAYER1_KALAH);

    }

    /**
     * This Utility method counts total number of stones in player2 Pits (8-13)
     *
     * @return a positive Integer value
     */
    public Integer getPlayer2PitStones() {

        return getTotalStones(Board.PLAYER1_KALAH + 1, Board.PLAYER2_KALAH);

    }

    private Integer getTotalStones(Integer startPitId, Integer endPitId) {

        return IntStream.range(startPitId, endPitId)
                .mapToObj(pitId -> this.getPits().get(pitId).getStoneCount())
                .collect(Collectors.summingInt(Integer::intValue));
    }
}


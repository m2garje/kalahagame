package com.backbase.game.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.util.Map;

/**
 * This class represent the Game API response information. A game response contain information about game identifier, path
 * and Key-Value information about current game state (Kalah pit id as Key and stones count as Value).
 *
 * @author Mahesh G
 */
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {

    private String id;
    private String url;

    @JsonInclude(Include.NON_EMPTY)
    private Map<String, String> status;
}

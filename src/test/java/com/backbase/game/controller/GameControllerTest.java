package com.backbase.game.controller;


import javax.annotation.PostConstruct;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backbase.game.model.GameResponse;

/**
 *
 * @author Mahesh G
 *
 */
@SpringBootTest
@WebAppConfiguration
public class GameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private static final String GAMES_URL = "/games";
    private static final String PITS_URL = "/pits/";

    @PostConstruct
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DirtiesContext
    public void createAGameAndFillDefaultStones_test() throws Exception {

        MockHttpServletRequestBuilder initGameRequest = MockMvcRequestBuilders.post(GAMES_URL);

        mockMvc.perform(initGameRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").doesNotExist())
                .andReturn();
    }

    @Test
    @DirtiesContext
    public void makeAMove_test() throws Exception {

        MockHttpServletRequestBuilder initGameRequest = MockMvcRequestBuilders.post(GAMES_URL);
        String responseString = mockMvc.perform(initGameRequest).andReturn().getResponse().getContentAsString();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        GameResponse gameResponse = objectMapper.readValue(responseString, GameResponse.class);


        MockHttpServletRequestBuilder playGame = MockMvcRequestBuilders.put(GAMES_URL + "/" + gameResponse.getId() + PITS_URL + 4);

        mockMvc.perform(playGame)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(gameResponse.getId()))		//check game id
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value(gameResponse.getUrl()))	//check game url
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.size()", Matchers.is(14)))		//check total pit size
                .andReturn();

    }

}

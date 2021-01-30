package com.backbase.game.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * This class is used to read properties from application.properties file
 */
@ConfigurationProperties(prefix = "kalahgame")
@Configuration
@Data
public class ApplicationConfig {


    @Autowired
    private Environment environment;

    @Value("${kalahgame.stones}")
    private int noOfStones;


}

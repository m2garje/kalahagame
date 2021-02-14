package com.backbase.game.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This configuration class is use to configure SwaggerUI api document to run
 * internal application controllers.
 *
 * @author Mahesh G
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "swagger")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private String title;
    private String description;
    private String version;
    private String name;
    private String url;
    private String email;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.backbase.game"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    @Bean
    /**
     * This method returns the specific plugin needed.To reslove conflict this was added.
     */
    public LinkDiscoverers discovers() {
        List plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }

    private ApiInfo getApiInfo() {

        return new ApiInfo(getTitle(), getDescription(), getVersion(), null, craterSwaggerApiDocContact(),
                null, null, Collections.emptyList());
    }

    private Contact craterSwaggerApiDocContact() {

        return new Contact(getName(), getUrl(), getEmail());
    }

}

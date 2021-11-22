package com.progenia.incubatis01_03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;
import org.vaadin.spring.events.annotation.EnableVaadinEventBus;

/**
 * The entry point of the Spring Boot application.
 */

/*
@SpringBootApplication = @Configuration + @ComponentScan + @EnableAutoConfiguration
*/
@SpringBootApplication
@EnableVaadinEventBus
public class Application extends SpringBootServletInitializer {

    //LOGING - Add log statements
    private static final Logger LOGGER=LoggerFactory.getLogger(Application.class);
 
    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));

        //LOGING - Add log statements
        LOGGER.info("Simple log statement with inputs {}, {} and {}", 1,2,3);
    }

}

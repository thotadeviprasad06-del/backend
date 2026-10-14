package com.skillexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Skill Exchange Platform - Main Spring Boot Application Entry Point.
 * B.Tech 3rd Year CSE Capstone Project.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class SkillExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillExchangeApplication.class, args);
    }
}

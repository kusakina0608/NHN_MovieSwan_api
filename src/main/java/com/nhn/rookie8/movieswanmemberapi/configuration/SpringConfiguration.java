package com.nhn.rookie8.movieswanmemberapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanmemberapi.dto.DatabaseInfoDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class SpringConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    @DependsOn({"restTemplate","objectMapper"})
    public DatabaseInfoDTO databaseInfoDTO(){
        restTemplate().getForObject().
    }
}

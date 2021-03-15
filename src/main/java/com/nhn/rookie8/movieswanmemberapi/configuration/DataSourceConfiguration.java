package com.nhn.rookie8.movieswanmemberapi.configuration;

import com.nhn.rookie8.movieswanmemberapi.dto.SecretDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Import(ContainerConfiguration.class)
public class DataSourceConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Autowired
    SecretDataDTO secretDataDTO;

    @Bean
    public DataSource getDataSource(){
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(secretDataDTO.getAccount().getDatabase().getUsername())
                .password(secretDataDTO.getAccount().getDatabase().getPassword())
                .build();
    }
}

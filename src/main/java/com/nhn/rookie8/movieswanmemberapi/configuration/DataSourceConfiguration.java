package com.nhn.rookie8.movieswanmemberapi.configuration;

import com.nhn.rookie8.movieswanmemberapi.datasource.RoutingDataSource;
import com.nhn.rookie8.movieswanmemberapi.dto.SecretDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Import(ContainerConfiguration.class)
public class DataSourceConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url1}")
    private String url1;

    @Value("${spring.datasource.url2}")
    private String url2;

    @Autowired
    SecretDataDTO secretDataDTO;

    @Bean
    public DataSource createRouterDatasource() {
        AbstractRoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("swan_account", getDataSource(url1));
        targetDataSources.put("swan_account2", getDataSource(url2));
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get("swan_account"));
        routingDataSource.setTargetDataSources(targetDataSources);
        return routingDataSource;
    }

    private DataSource getDataSource(String url){
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(secretDataDTO.getAccount().getDatabase().getUsername())
                .password(secretDataDTO.getAccount().getDatabase().getPassword())
                .build();
    }
}

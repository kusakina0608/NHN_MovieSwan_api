package com.nhn.rookie8.movieswanmemberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceTransactionManagerAutoConfiguration.class,
		DataSourceAutoConfiguration.class })
public class MovieswanMemberApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieswanMemberApiApplication.class, args);
	}

}

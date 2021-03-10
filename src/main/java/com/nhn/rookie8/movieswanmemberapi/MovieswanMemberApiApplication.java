package com.nhn.rookie8.movieswanmemberapi;

import com.nhn.rookie8.movieswanmemberapi.security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class MovieswanMemberApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieswanMemberApiApplication.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/api/register").permitAll()
					.antMatchers(HttpMethod.POST, "/api/auth").permitAll()
					.antMatchers(HttpMethod.POST, "/api/token").permitAll()
					.antMatchers(HttpMethod.POST, "/api/getMemberInfo").permitAll()
					.antMatchers(HttpMethod.POST, "/api/isExistId").permitAll()
					.anyRequest().authenticated();
		}
	}

}

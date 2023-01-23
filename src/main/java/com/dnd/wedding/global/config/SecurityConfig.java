package com.dnd.wedding.global.config;

import com.dnd.wedding.domain.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOauth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http
        .formLogin().disable()
        .httpBasic().disable()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorization")
        .and()
        .userInfoEndpoint()
        .userService(customOauth2UserService);

    return http.build();
  }
}

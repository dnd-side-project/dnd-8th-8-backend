package com.dnd.weddingmap.global.config;

import com.dnd.weddingmap.domain.jwt.JwtAuthenticationEntryPoint;
import com.dnd.weddingmap.domain.jwt.JwtAuthenticationFilter;
import com.dnd.weddingmap.domain.jwt.handler.JwtAccessDeniedHandler;
import com.dnd.weddingmap.domain.jwt.repository.CookieAuthorizationRequestRepository;
import com.dnd.weddingmap.domain.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.dnd.weddingmap.domain.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.dnd.weddingmap.domain.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOauth2UserService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
  private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
  private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/oauth2/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/jwt/refresh").permitAll()
        .anyRequest().authenticated();

    http
        .formLogin().disable()
        .httpBasic().disable()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorization")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository)
        .and()
        .userInfoEndpoint()
        .userService(customOauth2UserService)
        .and()
        .successHandler(oauth2AuthenticationSuccessHandler)
        .failureHandler(oauth2AuthenticationFailureHandler);

    http.exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler);

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
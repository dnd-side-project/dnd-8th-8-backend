package com.dnd.weddingmap.global.config;

import com.dnd.weddingmap.domain.jwt.JwtAuthenticationEntryPoint;
import com.dnd.weddingmap.domain.jwt.JwtAuthenticationFilter;
import com.dnd.weddingmap.domain.jwt.handler.JwtAccessDeniedHandler;
import com.dnd.weddingmap.domain.jwt.repository.CookieAuthorizationRequestRepository;
import com.dnd.weddingmap.domain.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.dnd.weddingmap.domain.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.dnd.weddingmap.domain.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

  @Value("${app.origin.url}")
  private String originUrl;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().configurationSource(corsConfigurationSource())
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin(originUrl);
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}

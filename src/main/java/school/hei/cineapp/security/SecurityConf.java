package school.hei.cineapp.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static school.hei.cineapp.security.model.UserRole.EMPLOYEE;
import static school.hei.cineapp.security.model.UserRole.MANAGER;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import school.hei.cineapp.security.exception.RestAccessDeniedHandler;
import school.hei.cineapp.security.exception.RestAuthenticationEntryPoint;
import school.hei.cineapp.security.filter.BearerAuthFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConf {
  private final RestAuthenticationEntryPoint entryPoint;
  private final RestAccessDeniedHandler accessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      BearerAuthFilter bearerAuthFilter,
      SelfAuthorizationManager selfAuthorizationManager)
      throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
        .exceptionHandling(
            e -> e.authenticationEntryPoint(entryPoint).accessDeniedHandler(accessDeniedHandler))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/register", "/login", "/ping")
                    .permitAll()
                    .requestMatchers(GET, "/movies", "/movies/**")
                    .permitAll()
                    .requestMatchers(GET, "/rooms", "/rooms/**")
                    .permitAll()
                    .requestMatchers(GET, "/projections")
                    .permitAll()
                    .requestMatchers(PUT, "/movies")
                    .hasRole(MANAGER.role())
                    .requestMatchers(PUT, "/rooms")
                    .hasRole(MANAGER.role())
                    .requestMatchers(PUT, "/rooms/*/seats")
                    .hasRole(MANAGER.role())
                    .requestMatchers(PUT, "/projection")
                    .hasRole(MANAGER.role())
                    .requestMatchers(GET, "/reservations")
                    .hasAnyRole(EMPLOYEE.role(), MANAGER.role())
                    .requestMatchers(PUT, "/reservation")
                    .hasAnyRole(EMPLOYEE.role(), MANAGER.role())
                    .requestMatchers(GET, "/reservations/*")
                    .authenticated()
                    .requestMatchers("/users/{uid}/reservations")
                    .access(selfAuthorizationManager)
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(bearerAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}

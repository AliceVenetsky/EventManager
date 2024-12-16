package com.example.eventmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtTokenFilter tokenFilter;
    @Autowired
    private CustomAuthenticationEntryPoint entryPoint;
    @Autowired
    private CustomAccessDeniedHandler deniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true).ignoring()
                .requestMatchers("/css/**",
                        "/js/**",
                        "/img/**",
                        "/lib/**",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/swagger-config",
                        "/openapi.yaml"
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(deniedHandler))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/locations").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/locations/{locationId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/locations/{locationId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/locations/**").hasAnyAuthority("ADMIN", "USER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
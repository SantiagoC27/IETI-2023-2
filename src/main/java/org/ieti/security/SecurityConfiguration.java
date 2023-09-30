package org.ieti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfiguration {

    JwtRequestFilter jwtRequestFilter;
    private final AuthenticationProvider authenticationProvider;


    public SecurityConfiguration(@Autowired JwtRequestFilter jwtRequestFilter, AuthenticationProvider authenticationProvider) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationProvider = authenticationProvider;
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf .disable())
                    .cors(cors -> cors .disable())
                    .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("v1/auth/**").permitAll()
                                .requestMatchers("/health").permitAll()
                                .anyRequest().authenticated()
                            )
                    .sessionManagement(sesionManager ->
                            sesionManager
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class)
                    .build();

    }
}

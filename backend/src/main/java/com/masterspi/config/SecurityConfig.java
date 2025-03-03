package com.masterspi.config;

import com.masterspi.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
            AuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/backoffice/login").permitAll()
                .requestMatchers("/backoffice/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/backoffice/**").hasAnyRole("ADMIN", "ESTOQUISTA")
                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                .loginPage("/backoffice/login")
                .loginProcessingUrl("/backoffice-login")
                .defaultSuccessUrl("/backoffice", true)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
                )
                .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
                );

        return http.build();
    }
}

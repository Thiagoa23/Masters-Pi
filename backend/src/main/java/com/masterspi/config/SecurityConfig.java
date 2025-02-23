package com.masterspi.config;

import com.masterspi.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
                .requestMatchers("/backoffice/login", "/public/**").permitAll()
                .requestMatchers("/backoffice/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/backoffice/**").hasAnyRole("ADMIN", "ESTOQUISTA")
                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                .loginPage("/backoffice/login")
                .loginProcessingUrl("/backoffice-login")
                .defaultSuccessUrl("/backoffice", true)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/backoffice/logout")
                .logoutSuccessUrl("/backoffice/login")
                .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder encoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }
}

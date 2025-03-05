/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.masterspi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class WebSecurityConfig {

    private final AuthenticationFailureHandler authenticationFailureHandler;

    public WebSecurityConfig(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/backoffice/login").permitAll()
                .requestMatchers("/backoffice/usuarios/**").hasAuthority("ADMIN")
                .requestMatchers("/backoffice/**").hasAnyAuthority("ADMIN", "ESTOQUISTA")
                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                .loginPage("/backoffice/login")
                .loginProcessingUrl("/backoffice-login")
                .defaultSuccessUrl("/backoffice", true)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/backoffice/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
                );

        return http.build();
    }

}

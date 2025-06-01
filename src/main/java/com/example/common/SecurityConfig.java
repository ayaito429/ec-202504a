package com.example.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring securityの認証・認可用クラス
 * 
 * @author shirota sho
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private CustomAuthenticationSuccessHandler successHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/toOrder", "/order", "/orderHistory", "/toOrder",
                                                                "/orderCo")
                                                .authenticated()
                                                .anyRequest().permitAll())
                                .formLogin(form -> form
                                                .loginPage("/toLogin")
                                                .loginProcessingUrl("/login")
                                                .successHandler(successHandler)
                                                .failureUrl("/toLogin?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/toLogin?logout=true")
                                                .invalidateHttpSession(true)
                                                .permitAll())
                                .userDetailsService(userDetailsService)
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }
}

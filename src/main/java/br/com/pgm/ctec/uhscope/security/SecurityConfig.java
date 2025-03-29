package br.com.pgm.ctec.uhscope.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF (caso não precise)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite todas as requisições sem autenticação
            )
            .formLogin(form -> form.disable()) // Desativa formulário de login
            .httpBasic(httpBasic -> httpBasic.disable()); // Desativa autenticação HTTP Basic

        return http.build();
    }
}
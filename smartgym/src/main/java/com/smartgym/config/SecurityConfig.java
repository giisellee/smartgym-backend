package com.smartgym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    // Injetamos o nosso Guarda-Costas aqui
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                // A API não vai guardar "sessão" (estado) na memória. Quem guarda o estado é o Token.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {

                    // REGRAS DE PORTA DE ENTRADA:
                    // 1. Liberar a rota de Login para qualquer um
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();

                    // 2. Exemplo de RBAC: Só o GERENTE pode inativar academias
                    req.requestMatchers(HttpMethod.DELETE, "/academias/*").hasRole("GERENTE");

                    // 3. Qualquer outra requisição, de qualquer rota, TEM que estar autenticada
                    req.anyRequest().authenticated();
                })
                // Pede ao Spring para rodar o nosso SecurityFilter ANTES do filtro padrão dele
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Ensina o Spring a injetar o AuthenticationManager no nosso AutenticacaoController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
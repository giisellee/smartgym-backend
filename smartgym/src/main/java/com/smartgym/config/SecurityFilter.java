package com.smartgym.config;

import com.smartgym.repository.UsuarioRepository;
import com.smartgym.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Diz ao Spring para carregar este filtro na memória
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    // Este método roda UMA VEZ a cada requisição que chega na API
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Pega o token do cabeçalho
        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // 2. Lê a "pulseira" e descobre de quem é o e-mail
            String subject = tokenService.getSubject(tokenJWT);

            // 3. Puxa os dados da pessoa do banco
            var usuario = usuarioRepository.findByEmail(subject);

            // 4. Força o login no Spring Security (Avisa: "Pode deixar, eu já conferi o token dele!")
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. Manda a requisição seguir o seu caminho (para o Controller)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair a palavra "Bearer " e deixar só o código do Token
    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
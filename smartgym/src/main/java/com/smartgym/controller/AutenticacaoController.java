package com.smartgym.controller;

import com.smartgym.model.Usuario;
import com.smartgym.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    // Usamos um Record (recurso moderno do Java) para não ter de criar classes extras para o JSON
    public record DadosAutenticacao(String email, String senha) {}
    public record DadosTokenJWT(String token) {}

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody DadosAutenticacao dados) {
        try {
            // 1. Cria o pacote com os dados que o utilizador digitou
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

            // 2. O Spring vai lá no AutenticacaoService e testa se a senha bate com a do banco
            var authentication = manager.authenticate(authenticationToken);

            // 3. Se deu certo, chama a nossa fábrica de pulseiras para gerar o JWT
            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

            // 4. Devolve o Token na tela para o Frontend guardar
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("E-mail ou senha incorretos.");
        }
    }
}
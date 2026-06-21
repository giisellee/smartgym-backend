package com.smartgym.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.smartgym.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service // Marca a classe como um componente de negócio do Spring
public class TokenService {

    /**
     * A "chave de segurança" ou "carimbo" do nosso servidor.
     * O @Value vai ao ficheiro application.properties ler a variável que definimos.
     * Se alguém tentar falsificar um token, mas não tiver esta chave secreta,
     * a assinatura digital vai falhar e o nosso servidor vai rejeitar o token.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * MÉTODO: GERAR TOKEN
     * Chamado apenas quando o utilizador acerta o e-mail e a palavra-passe no login.
     * Fabrica a "Pulseira VIP" (Token JWT).
     */
    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de criptografia pesada (HMAC256) usando a nossa chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    // .withIssuer -> Define "quem" criou este token (A nossa aplicação: smartgym-api)
                    .withIssuer("smartgym-api")

                    // .withSubject -> Qual é a informação principal guardada? (O e-mail do utilizador)
                    .withSubject(usuario.getEmail())

                    // .withClaim -> A MÁGICA DO MULTITENANT!
                    // Guardamos o ID da filial "escondido" dentro do token.
                    // Assim, não precisamos de perguntar à base de dados qual é a filial dele em todas as requisições!
                    .withClaim("idAcademia", usuario.getAcademia().getId())

                    // .withExpiresAt -> Até quando o token é válido?
                    .withExpiresAt(gerarDataExpiracao())

                    // .sign -> Sela e assina o token com a nossa chave secreta
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            // Se faltar a chave secreta ou ocorrer um erro de algoritmo, o sistema para aqui.
            throw new RuntimeException("Erro interno ao tentar gerar o token JWT", exception);
        }
    }

    /**
     * MÉTODO: VALIDAR E LER TOKEN
     * Chamado em TODAS as requisições que o utilizador fizer após o login (ex: listar alunos).
     * O nosso "Guarda-Costas" vai usar este método para abrir a pulseira VIP e ver de quem é.
     */
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    // Confirma se fomos nós mesmos ("smartgym-api") que criámos o token
                    .withIssuer("smartgym-api")
                    .build()
                    // Tenta abrir o token. Se tiver sido alterado por um hacker ou se já passou das 2 horas, ESTOURA UM ERRO.
                    .verify(tokenJWT)
                    // Se estiver tudo perfeito, devolve o e-mail que guardámos lá no gerarToken()
                    .getSubject();

        } catch (JWTVerificationException exception) {
            // Se estourar erro (token falso ou expirado), devolve uma string vazia.
            // O Spring Security vai ver a string vazia e vai dar um "Acesso Negado" (Status 403).
            return "";
        }
    }

    /**
     * MÉTODO PRIVADO: CALCULAR TEMPO DE VIDA
     * Tokens de segurança não podem durar para sempre. Se um utilizador for a um café
     * e deixar o computador aberto, o token tem de expirar para segurança.
     */
    private Instant gerarDataExpiracao() {
        // Pega a hora exata de AGORA, soma 2 HORAS, e converte para o fuso horário oficial (ZoneOffset)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
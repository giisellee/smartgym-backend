package com.smartgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
// Usamos JOINED porque Aluno e Funcionario possuem muitos campos específicos agora
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter

//1. Implementando a interface UserDetails do Spring Security
public abstract class Usuario implements UserDetails { // A palavra 'abstract' impede que alguém instancie um "Usuario" genérico

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String telefone;

    // Atualizado para LocalDateTime
    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    // Criação da chave estrangeira idAcademia
    @ManyToOne
    @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;

    // Controle de Deleção Lógica
    // Todo usuário recém-criado nasce ativo no sistema
    private Boolean ativo = true;


    // ========================================================================
    // MÉTODOS OBRIGATÓRIOS DA INTERFACE 'UserDetails' DO SPRING SECURITY
    // ========================================================================

    /**
     * getAuthorities(): O Sistema de Permissões (Roles)
     * O Spring Security não entende as nossas classes Java. Ele entende "Autoridades".
     * Este método pega na classe real da pessoa (ex: "Gerente") e converte para
     * o padrão oficial do Spring (ex: "ROLE_GERENTE").
     * É este método que permite usar o @PreAuthorize("hasRole('GERENTE')") nos Controllers.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Ele lê o nome da classe filha (Gerente, Aluno, Atendente) e transforma no formato do Spring (ex: ROLE_GERENTE)
        String role = "ROLE_"+ this.getClass().getSimpleName().toUpperCase();
        // 3. Devolve a permissão na "linguagem" que o Spring Security entende
        return List.of(new SimpleGrantedAuthority(role));
    }


    /**
     * getPassword(): Onde está a palavra-passe?
     * O Spring vai usar este método nos bastidores quando o utilizador tentar fazer login
     * para comparar a palavra-passe digitada com a que está na base de dados.
     */
    @Override
    public String getPassword() {
        return this.senha;
    }

    /**
     * getUsername(): Qual é o identificador de login?
     * Por padrão, o Spring procura um "nome de utilizador". Como no nosso sistema
     * o login é feito através do e-mail, dizemos ao Spring para usar o e-mail aqui.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /* * Os próximos 3 métodos servem para verificar se a conta expirou ou foi bloqueada.
     * Como não temos essa lógica específica de "conta bloqueada por tentativas erradas",
     * devolvemos sempre 'true' (conta válida).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * isEnabled(): A conta está ativa?
     * Aqui unimos a nossa lógica de Deleção Lógica (Soft Delete) com o Spring.
     * Se um funcionário for demitido (ativo = false), o Spring Security lerá
     * este método e bloqueará o login dele automaticamente, devolvendo o erro 403 (Forbidden).
     */
    public boolean isEnabled() {
        return this.ativo;
    }
}

package com.smartgym.repository;

import com.smartgym.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método que o Spring vai usar para procurar a pessoa pelo email na hora do login
    UserDetails findByEmail(String email);
}

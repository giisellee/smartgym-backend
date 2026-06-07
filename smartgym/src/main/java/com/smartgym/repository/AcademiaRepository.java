package com.smartgym.repository;

import com.smartgym.model.Academia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Criando interface que se comunica com o banco de dados
public interface AcademiaRepository extends JpaRepository<Academia, Long>  { // A interface herda tudo de JpaRepository, sendo Academia a tabela manipulada e o Long sendo o tipo da chave primária
    // NOTAÇÃO SQL: SELECT COUNT(*) > 0 FROM academia WHERE nome = ?
    boolean existsByNome(String nome); // Não permite salvar uma academia com o mesmo nome de uma outra já existente no banco, evitando uma tela bem feia.
}

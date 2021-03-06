package spring.modelo.relacional.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import spring.modelo.relacional.domain.Estado;

import java.util.List;

//camada acesso a dados 
@Repository
//JpaRepository<obj do tipo categoria, tipo do atributo identificador>--tipo especial do spring para acessar os dados
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    @Transactional(readOnly = true)
    List<Estado> findAllByOrderByNome();
}

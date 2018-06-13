package spring.modelo.relacional.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.modelo.relacional.domain.Cliente;

//camada acesso a dados 
@Repository
//JpaRepository<obj do tipo categoria, tipo do atributo identificador>--tipo especial do spring para acessar os dados
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
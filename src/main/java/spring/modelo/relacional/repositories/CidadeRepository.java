package spring.modelo.relacional.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import spring.modelo.relacional.domain.Cidade;
import spring.modelo.relacional.domain.Estado;

import java.util.List;

//camada acesso a dados 
@Repository
//JpaRepository<obj do tipo categoria, tipo do atributo identificador>--tipo especial do spring para acessar os dados
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {


    //@Query("Select obj FROM Cidade obj WHERE obj.estado.id = :estadoId ORDER BY obj.nome")
    //public List<Cidade> findCidade(@Param("estadoId") Integer estado_id);

    @Transactional(readOnly=true)
    public List<Cidade> findByEstado(Estado estado);
}

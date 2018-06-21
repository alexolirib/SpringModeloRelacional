package spring.modelo.relacional.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.domain.Produto;

//camada acesso a dados 
@Repository
//JpaRepository<obj do tipo categoria, tipo do atributo identificador>--tipo especial do spring para acessar os dados
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
	//esse método não tem a implementação desse método no spring preciso botar @Query(aqui fica o JPQL)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome" /* nome dos paramétro no JPQL*/) String nome,@Param("categorias") List<Categoria> categorias, Pageable linePerPage);

}

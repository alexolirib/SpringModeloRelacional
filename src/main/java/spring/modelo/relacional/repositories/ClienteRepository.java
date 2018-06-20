package spring.modelo.relacional.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import spring.modelo.relacional.domain.Cliente;

//camada acesso a dados 
@Repository
//JpaRepository<obj do tipo categoria, tipo do atributo identificador>--tipo especial do spring para acessar os dados
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	//criando o nome seguindo esse padrão com o campo o próprio spring 
	//cria esse método automáticamente para mim (ou seja busca pelo email)
	@Transactional(readOnly=true)//que essa operação não precisa ter transação com o nosso banco (isso faz nossa operação ser mais rápida)
	Cliente findByEmail(String email);
}

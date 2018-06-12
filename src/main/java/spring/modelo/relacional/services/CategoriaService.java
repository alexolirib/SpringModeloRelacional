package spring.modelo.relacional.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.repositories.CategoriaRepository;

//camada de serviço
@Service
public class CategoriaService {
	
	//injeção de dependencia
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		//Optional obj container (encapsular se o obj está instanciado ou não)
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
	}
	
}

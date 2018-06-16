package spring.modelo.relacional.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.repositories.CategoriaRepository;
import spring.modelo.relacional.services.Exception.DataIntegrityException;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

//camada de serviço
@Service
public class CategoriaService {

	// injeção de dependencia
	@Autowired
	private CategoriaRepository repo;

	public Categoria findById(Integer id) {
		// Optional obj container (encapsular se o obj está instanciado ou não)
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id:" + id + ", tipo:" + Categoria.class.getName()));// nome da classe
	}

	public Categoria insert(Categoria obj) {
		// o id precisa ser obrigatoriamente null para poder fazer o save, se não o
		// spring pensa que é
		// um update
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		findById(obj.getId());// verifica se esse objeto existe
		return repo.save(obj);
	}

	public void delete(Integer id) {
		//verifica se o id existe 
		findById(id);

		//fazendo isso pois se tentar remover algum obj que tenha objetos associados com ele
		//quero que der uma exceção personalizada, em vez de erro 500
		try {
			repo.deleteById(id);
		} catch (Exception e) {
			throw new DataIntegrityException("Não é possível excluir categoria que não possui produto");
		}
	}

	public List<Categoria> findAll() {
		
		return repo.findAll();
	}

}

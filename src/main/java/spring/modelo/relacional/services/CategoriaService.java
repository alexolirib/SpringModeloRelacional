package spring.modelo.relacional.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.dto.CategoriaDTO;
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
		Categoria newObj = findById(obj.getId());// verifica se esse objeto existe
		updateDate(newObj, obj);		
		return repo.save(newObj);
	}

	private void updateDate(Categoria newObj, Categoria obj) {
		 newObj.setNome(obj.getNome());
		
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
	
	//Page capsula informações sobre a pginação 
	//findPage(Qual pagina eu quero, numero de linha na pagina , ordenar por qual atributo,  direção (arcendente  ou decescente )
	public Page<Categoria> findPage(Integer page, Integer linesPerPAge, String orderBy, String direction){
		//Direction.valueOf (converte tudo para direction)
		PageRequest pageRequest = PageRequest.of(page, linesPerPAge, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	//instancia uma categoria partir do DTO
	public Categoria fromDto(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}

}

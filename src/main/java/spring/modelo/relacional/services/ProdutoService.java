package spring.modelo.relacional.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.domain.Produto;
import spring.modelo.relacional.repositories.CategoriaRepository;
import spring.modelo.relacional.repositories.ProdutoRepository;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto findById(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado "+ id + ", tipo " + Produto.class.getName()));
	}
	
	//com um id produto irá vir lista de id de categoria	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linePerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linePerPage, Direction.valueOf(direction), orderBy);
		
		//buscando as categorias com a lista de id daquele produto
		List<Categoria> categorias =  categoriaRepository.findAllById(ids);
		//return repo.search(nome, categorias, pageRequest);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);

	}

	public List<Produto> findAll() {
		return repo.findAll();		
	}
}

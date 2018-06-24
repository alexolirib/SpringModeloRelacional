package spring.modelo.relacional.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.domain.Produto;
import spring.modelo.relacional.dto.ProdutoNewDto;
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

	public List<Categoria> pushCategorias(@Valid ProdutoNewDto objDto) {
		List<Categoria> categorias = new ArrayList<>();
		categorias.add(findByIdCategoria(Integer.parseInt(objDto.getCategoriaId1())));
		if(objDto.getCategoriaId2() != null) {
			categorias.add(findByIdCategoria(Integer.parseInt(objDto.getCategoriaId2())));
		}
		if(objDto.getCategoriaId3() != null) {
			categorias.add(findByIdCategoria(Integer.parseInt(objDto.getCategoriaId3())));
		}
		if(objDto.getCategoriaId4() != null) {
			categorias.add(findByIdCategoria(Integer.parseInt(objDto.getCategoriaId4())));
		}
		if(objDto.getCategoriaId5() != null) {
			categorias.add(findByIdCategoria(Integer.parseInt(objDto.getCategoriaId5())));
		}
		return categorias;
	}
	
	private Categoria findByIdCategoria(Integer id) {
		// Optional obj container (encapsular se o obj está instanciado ou não)
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id:" + id + ", tipo:" + Categoria.class.getName()));// nome da classe
	}

	public Produto fromDto(@Valid ProdutoNewDto objDto, List<Categoria> categorias) {
		Produto obj = new Produto(null, objDto.getNome(), Double.parseDouble(objDto.getPreco()));
		
		obj.getCategorias().addAll(categorias);
		
		return obj;
	}

	@Transactional
	public Produto insert(Produto obj, List<Categoria> categorias) {
		obj.setId(null);
		obj = repo.save(obj);
		for(Categoria cat : categorias) {
			cat.getProdutos().add(obj);
			categoriaRepository.save(cat);
		}
		return obj;
	}
}

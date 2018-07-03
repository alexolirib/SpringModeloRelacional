package spring.modelo.relacional.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import spring.modelo.relacional.domain.Categoria;
import spring.modelo.relacional.domain.Produto;
import spring.modelo.relacional.dto.ProdutoDTO;
import spring.modelo.relacional.dto.ProdutoNewDto;
import spring.modelo.relacional.resource.utils.URL;
import spring.modelo.relacional.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable Integer id){
		Produto obj = service.findById(id);

		return ResponseEntity.ok().body(obj);
	}
	
	//vai ter que passar como parametro na url essa consulta
	@RequestMapping(value="/page",method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value="nome", defaultValue="") String nome,
			@RequestParam(value="categorias", defaultValue="") String categorias,
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linePerPage", defaultValue="24") Integer linePerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction){
		
		List<Integer> ids = URL.decodeIntList(categorias);
		String nomeDecoded = URL.decodeParam(nome);
		
		
		Page<Produto> obj = service.search(nomeDecoded, ids, page, linePerPage, orderBy, direction);
		Page<ProdutoDTO> listDTO = obj.map(p-> new ProdutoDTO(p));
		
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(value= "/", method=RequestMethod.GET)
	public ResponseEntity<List<Produto>> findAll(){
		List<Produto> listObj = service.findAll();
		return ResponseEntity.ok().body(listObj);
	}
	

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value= "/", method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ProdutoNewDto objDto){
		List<Categoria> categorias = service.pushCategorias(objDto);
		Produto obj = service.fromDto(objDto, categorias);
		
		obj = service.insert(obj, categorias);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
}

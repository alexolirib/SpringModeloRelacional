package spring.modelo.relacional.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
import spring.modelo.relacional.dto.CategoriaDTO;
import spring.modelo.relacional.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias") // nome do endpoint
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {

		Categoria obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	
	/*@RequestMapping(method = RequestMethod.POST)
	// responde com corpo vazio
	// @RequestBody converte para um obj
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		obj = service.insert(obj);
		// quando insere a resposta http deve ser 201 e retorna a urI novo recurso
		// criado
		// fromCurrentRequest()- pega o http://localhost:8080/categorias (que vai ser
		// baseada nela)
		// .path("/{id}") - ja que é /(id) é pego através disso
		// buildAndExpand(obj.getId()) - para atribuir o valor nesse .path("/{id}"),
		// pegando o ultimo obj adicionado
		// .toUri() converte para uri
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// como é o corpo vazio a resposta vem no header da req
		return ResponseEntity.created(uri).build();
	}*/
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO) {
		Categoria obj = service.fromDto(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	/*@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
		obj.setId(id); // quiser fazer isso só por segurança
		obj = service.update(obj);
		// conteudo vazio
		return ResponseEntity.noContent().build();
	}*/
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDTO, @PathVariable Integer id) {
		Categoria obj = service.fromDto(objDTO);
		obj.setId(id); // quiser fazer isso só por segurança
		obj = service.update(obj);
		// conteudo vazio
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		service.delete(id);

		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List</* Categoria */ CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll();
		// percorrer a lista o -stream()
		// list.stream() converte a lista fique do tipo stream
		// map() função percorre coleção do tipo stream
		// collect(Collectors.toList()); converte de volta uma lista stream para uma
		// normal
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	// requestMapping(parametro opcional)
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findAll(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPAge", defaultValue="24") Integer linesPerPAge,//padrão melhor 24, pois é multiplo de 1,2,3,4 e mais fácil de trabalhar
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC"/*pode ser ou ASC ou DESC*/) String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPAge, orderBy, direction);
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj));

		return ResponseEntity.ok().body(listDTO);
	}
	
	
}

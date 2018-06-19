package spring.modelo.relacional.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.dto.ClienteDTO;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado! id: "
				+ id +", tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {
		
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linePerPages, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linePerPages, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente Update(Cliente cliente) {
		findById(cliente.getId());
		return repo.save(cliente);
	}
	
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		//retorna null os campos que o DTO não possui
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
}

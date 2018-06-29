package spring.modelo.relacional.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Endereco;
import spring.modelo.relacional.repositories.EnderecoRepository;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository repo;
	
	public Endereco findById(Integer id) {
		Optional<Endereco> endereco = repo.findById(id);
		return endereco.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado! : id"
				+ id + ", tipo: " + Endereco.class.getName()));
	}
}

package spring.modelo.relacional.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.security.UserSS;

//UserDetailsService- contrato que é feito para fazer a busca pelo o nome do usuario 
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli =  repo.findByEmail(email);
		//que não existe
		if(cli == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
		
	}

}

package spring.modelo.relacional.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import spring.modelo.relacional.domain.Cidade;
import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.domain.Endereco;
import spring.modelo.relacional.domain.enums.Perfil;
import spring.modelo.relacional.domain.enums.TipoCliente;
import spring.modelo.relacional.dto.ClienteDTO;
import spring.modelo.relacional.dto.ClienteNewDto;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.repositories.EnderecoRepository;
import spring.modelo.relacional.security.UserSS;
import spring.modelo.relacional.services.Exception.AuthorizationException;
import spring.modelo.relacional.services.Exception.DataIntegrityException;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	//criptografar a senha (injeção ocorre no arquivo securityConfig)
	@Autowired
	private BCryptPasswordEncoder bcpe;

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;

	public Cliente findById(Integer id) {
		//pega o usuario logado
		UserSS user = UserService.authenticad();
		//se o cliente logado não for ADMIN e não for o cliente do id
		//solicitado, lançar uma exceção
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id: " + id + ", tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {

		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linePerPages, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linePerPages, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente Update(Cliente cliente) {
		Cliente newCliente = findById(cliente.getId());
		updateDate(newCliente, cliente);
		return repo.save(newCliente);
	}

	private void updateDate(Cliente newCliente, Cliente cliente) {
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());
	}

	public Cliente fromDTO(ClienteDTO objDTO) {
		// retorna null os campos que o DTO não possui
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDto objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(),
				objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()),
				bcpe.encode(objDTO.getSenha()));/*bcpe.encode(criptografa a string, ou seja a senha*/
		//escontrar a cidade 
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		
		Endereco end = new Endereco(null , objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		
		if(objDTO.getTelefone2() != null) {

			cli.getTelefones().add(objDTO.getTelefone2());
		}
		
		if(objDTO.getTelefone3() != null) {

			cli.getTelefones().add(objDTO.getTelefone3());
		}
		
		return cli;
	}

	public void remove(Integer id) {
		findById(id);
		
		try {
			repo.deleteById(id);
		} catch (Exception e) {
			throw new DataIntegrityException("Não é possível excluir ,pois tem pedidos relacionados");
		}
	}
	
	//já que irá salvar mais de 1 coisa no banco
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());		
		return 	obj;	
	}
	
	public URI uploadProfilePictore(MultipartFile multipartFile) {
		//verifica user logado
		UserSS user = UserService.authenticad();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		//para extrair um jpg partir de um arquivo que foi enviado
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg")
				, fileName, "image");
	}

	public Cliente findByEmail(String email){
		UserSS user = UserService.authenticad();
		//se é nulo ou igual o admin
		if(user == null|| !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())){
			throw new AuthorizationException("Acesso negado");
		}

		Cliente obj =  repo.findByEmail(email);
		if(obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! id " + user.getId() + ", Tipo " + Cliente.class.getName());
		}
		return  obj;
	}

}

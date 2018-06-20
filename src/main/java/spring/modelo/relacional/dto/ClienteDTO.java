package spring.modelo.relacional.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	//nunca irá poder trocar cpf ou cnpj o cliente por isso só boto o id-nome-email
	
	private Integer id;
	
	@NotEmpty(message="Preechimento obrigatório")
	@Length(min=5, max=80, message="O tamanho da categoria deve ter entre 5 a 80 caractera")
	private String nome;
	
	@NotEmpty(message="Preechimento obrigatório")
	@Email(message="e-mail inválido")
	private String email;
	
	
	
	public ClienteDTO() {
		super();
	}
	public ClienteDTO(Cliente cliente) {
		super();
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.email = cliente.getEmail();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}

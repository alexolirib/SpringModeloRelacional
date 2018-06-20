package spring.modelo.relacional.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import spring.modelo.relacional.services.validation.ClienteInsert;

//4º vindo da camada de serviço irei inserir no DTO a anotation customizada 
@ClienteInsert
public class ClienteNewDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//adicionar junto com o cliente um endereco com telefones
	@NotEmpty(message="Preechimento obrigatório")
	@Length(min=5, max=80, message="O tamanho da categoria deve ter entre 5 a 80 caractera")
	private String nome;
	
	@NotEmpty(message="Preechimento obrigatório")
	@Email(message="e-mail inválido")
	private String email;
	
	//como cpf e cnpj depende do tipo que o usuário informar teremos que fazer
	//anotation customizada para essa validação de campo
	//ou então existe a anotação @CPF e @CNPJ
	@NotEmpty(message="Preechimento obrigatório")
	private String cpfOuCnpj;
	private Integer tipo;
	
	@NotEmpty(message="Preechimento obrigatório")
	private String logradouro;
	
	@NotEmpty(message="Preechimento obrigatório")
	private String numero;
	private String complemento;
	private String bairro;
	
	@NotEmpty(message="Preechimento obrigatório")
	private String cep;

	@NotEmpty(message="Preechimento obrigatório")
	private String telefone1;
	private String telefone2;
	private String telefone3;
	
	private Integer cidadeId;

	public ClienteNewDto() {
		super();
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

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getTelefone3() {
		return telefone3;
	}

	public void setTelefone3(String telefone3) {
		this.telefone3 = telefone3;
	}

	public Integer getCidadeId() {
		return cidadeId;
	}

	public void setCidadeId(Integer cidadeId) {
		this.cidadeId = cidadeId;
	}
}

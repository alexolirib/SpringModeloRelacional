package spring.modelo.relacional.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class ProdutoNewDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min=5, max=80, message="O tamanho da categoria deve ter entre 5 a 80 caractera")
	private String nome;
	
	@NotEmpty(message="Preenchimento obrigatório")
	private String preco;
	
	@NotEmpty(message="Preenchimento obrigatório")
	private String categoriaId1;
	private String categoriaId2;
	private String categoriaId3;
	private String categoriaId4;
	private String categoriaId5;
	
	public ProdutoNewDto() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getCategoriaId1() {
		return categoriaId1;
	}

	public void setCategoriaId1(String categoriaId1) {
		this.categoriaId1 = categoriaId1;
	}

	public String getCategoriaId2() {
		return categoriaId2;
	}

	public void setCategoriaId2(String categoriaId2) {
		this.categoriaId2 = categoriaId2;
	}

	public String getCategoriaId3() {
		return categoriaId3;
	}

	public void setCategoriaId3(String categoriaId3) {
		this.categoriaId3 = categoriaId3;
	}

	public String getCategoriaId4() {
		return categoriaId4;
	}

	public void setCategoriaId4(String categoriaId4) {
		this.categoriaId4 = categoriaId4;
	}

	public String getCategoriaId5() {
		return categoriaId5;
	}

	public void setCategoriaId5(String categoriaId5) {
		this.categoriaId5 = categoriaId5;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}	

}

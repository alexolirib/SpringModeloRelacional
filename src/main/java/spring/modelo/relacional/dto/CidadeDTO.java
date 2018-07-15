package spring.modelo.relacional.dto;

import spring.modelo.relacional.domain.Cidade;

import java.io.Serializable;

public class CidadeDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;

    public CidadeDTO(Cidade cid) {
        this.id = cid.getId();
        this.nome = cid.getNome();
    }

    public CidadeDTO() {
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
}

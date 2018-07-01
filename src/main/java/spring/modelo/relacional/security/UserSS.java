package spring.modelo.relacional.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import spring.modelo.relacional.domain.enums.Perfil;

//SS - Spring Security
//esse user Details é um contrato que o spring precisa para trabalhar
//com usuarios
public class UserSS implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	// lista de perfis
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS(Integer id, String email,
			String senha, Set<Perfil> perfil /*<- ,em vez -> Collection<? extends GrantedAuthority> authorities*/) {
		super();
		this.id = id;
		this.email = email;	
		this.senha = senha;
		this.authorities = perfil.stream().map(p-> new SimpleGrantedAuthority(p.getDescricao())).collect(Collectors.toList()) /*authorities*/;
	}	

	public UserSS() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// se quiser implementar uma lógica para expirar a conta de um usuário
		// pode colocar nesse exato lugar
		return true;// padrão que não está expirada
	}

	@Override
	public boolean isAccountNonLocked() {
		// conta bloqueada
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// conta expirada
		return true;
	}

	@Override
	public boolean isEnabled() {
		// usuario ativo?
		return true;
	}

}

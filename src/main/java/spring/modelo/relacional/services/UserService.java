package spring.modelo.relacional.services;

import org.springframework.security.core.context.SecurityContextHolder;

import spring.modelo.relacional.security.UserSS;

//retorna usuario logado a classe
public class UserService {
	
	// ou seja o usuario logado no sistema
	public static UserSS authenticad() {
		//exception se não tiver logado
		try {
		return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		} catch (Exception e) {
			return null;
		}
	}
}

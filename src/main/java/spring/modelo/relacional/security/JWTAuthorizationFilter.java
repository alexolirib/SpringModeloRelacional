package spring.modelo.relacional.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//filtro de autorizações (analisar o token se for válido)
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
	
	private JWTUtil jwtUtil;
	
	//vai precisar para fazer busca do usuario pelo email
	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			 UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	//metodo padrão do filtro que vai executar algo antes de deixar continuar
	//1º pegar o valor que é mandado na requisição
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain) throws IOException, ServletException {
		//vai receber o cabeçalho a autorização que será o - Bearer ...
		String header = request.getHeader("Authorization");
		//verifica se o cabeçalho veio e se começa com Bearer
		if (header != null && header.startsWith("Bearer ")) {
			//getAuthentication - mandar o valor que está na frente de Bearer e retorna esse 
			//UsernamePasswordAuthenticationToken
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			//retorna null quando for inválido
			if (auth != null) {
				//função que libera o acesso no filtro
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			//falar para o filtro que pode continuar execução normal da requisição 
			//chain.doFilter(req, res);
		}
		chain.doFilter(request, response);
		
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if(jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			//buscar DB o usuario
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}

}

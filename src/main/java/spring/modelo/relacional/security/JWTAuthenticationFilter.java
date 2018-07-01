package spring.modelo.relacional.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.modelo.relacional.dto.CredenciaisDTO;

//colocar o filtro nas autenticações 
//UsernamePasswordAuthenticationFilter -> spring sabe que esse filtro terá que interceptar,
//requisição de login que é o /login (esse endpoint já é reservado do spring security)
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	
	private JWTUtil jwtUtil;	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		//para quando email e senha tiver errado aparecer erro 401
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	//tenta autenticar
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res) throws AuthenticationException{
		try {
		//req é a requisição vai pegar e converver para CredenciaisDTO
		CredenciaisDTO creds = new ObjectMapper()
							.readValue(req.getInputStream(), CredenciaisDTO.class);
		
		//depois de pegar os dados (não é o token do jwt é do Spring security)
		//tenho que passar o usuario , senha e a lista(vazia)
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
		
		//.authenticate(ele que vai verificar se o usuario e senha são válidos)
		//ele faz isso de acordo que foi implementado em UserDetailsService
		Authentication auth = authenticationManager.authenticate(authToken);
		return auth;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	//se autenticação ocorrer ocm sucesso o que fazer aqui!
	//que básicamente gerar um token e colocar na resposta da requisição 
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
/* parametro foi produzido no método de tentativa*/	Authentication auth) throws IOException, ServletException{
		//.getPrincipal() - usuário do spring, partir daí pega o email do usuario
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		//gera o token
		String token = jwtUtil.generateToken(username);
		//add no cabeçalho da resposta
		res.addHeader("Authorization", "Bearer " + token);
		
	}
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		 @Override
	        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
	                throws IOException, ServletException {
	            response.setStatus(401);
	            response.setContentType("application/json"); 
	            response.getWriter().append(json());
	        }
	        
	        private String json() {
	            long date = new Date().getTime();
	            return "{\"timestamp\": " + date + ", "
	                + "\"status\": 401, "
	                + "\"error\": \"Não autorizado\", "
	                + "\"message\": \"Email ou senha inválidos\", "
	                + "\"path\": \"/login\"}";
	}
		
	}
}

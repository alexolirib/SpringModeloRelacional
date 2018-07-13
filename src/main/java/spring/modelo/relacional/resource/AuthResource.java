package spring.modelo.relacional.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import spring.modelo.relacional.dto.EmailDTO;
import spring.modelo.relacional.security.JWTUtil;
import spring.modelo.relacional.security.UserSS;
import spring.modelo.relacional.services.AuthService;
import spring.modelo.relacional.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;
	
	//onde será o refresh token, quando tiver expirando será pego daqui!
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse res){
		UserSS user = UserService.authenticad();
		//aqui gera um novo token para o usuario 
		String token = jwtUtil.generateToken(user.getUsername());
		//adiciona o token na resposta
		res.addHeader("Authorization", "Bearer " + token);
		res.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO obDto ){
		service.sendNewPassword(obDto.getEmail());
		return ResponseEntity.noContent().build();
	}
}

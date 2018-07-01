package spring.modelo.relacional.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//para poder ser injetada em outras classes como componentes 
@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	//gerar um token
	public String generateToken(String username) {
		//.builder()- gera um token
		return Jwts.builder()
				.setSubject(username) //usuario
				.setExpiration(new Date(System.currentTimeMillis()+expiration))//baseado o tempo de expiração
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()) //como vou assinar o token signWith(qual o alg, qual o segredo)
				.compact();//final compactar        secret.getBytes()-tem que ser um array de bytes
	}
}

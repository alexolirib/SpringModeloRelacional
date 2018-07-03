package spring.modelo.relacional.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	//test validade do token
	public boolean tokenValido(String token) {
		// armazena as revindicações do token (usuario e tempo de expiração )
		Claims claims = getClaims(token);
		if(claims != null) {
			//obter o username
			String username= claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			//now.before(expirationDate) se o tempo atual é menor que a expiração(vai ta válido_
			if(username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	private Claims getClaims(String token) {
		try {
		//esse é a função que recupera os Claims partir de um token
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
	
		} catch (Exception e) {
			return null;
		}

	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if(claims != null) {
			return claims.getSubject();
		}
		return null;
	}
}

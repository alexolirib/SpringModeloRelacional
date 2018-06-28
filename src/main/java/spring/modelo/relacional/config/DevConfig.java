package spring.modelo.relacional.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import spring.modelo.relacional.services.DBService;
import spring.modelo.relacional.services.EmailService;
import spring.modelo.relacional.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	//verificar qual é a estratégia do banco se deve criar ou não (arquivo= application-dev.properties)
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		//se não for create aqui informo que não deve recriar a base de dados
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantieTestDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}

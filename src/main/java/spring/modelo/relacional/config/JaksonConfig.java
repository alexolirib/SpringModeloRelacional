package spring.modelo.relacional.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.modelo.relacional.domain.PagamentoComBoleto;
import spring.modelo.relacional.domain.PagamentoComCartao;

//classe de configurado estará disponivel no sistema e será executad ano início do sistema
@Configuration
public class JaksonConfig {
	//@Bean são os métodos que irá conter informações de configuração 
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
				super.configure(objectMapper);
			}
		};
		return builder;
	}
}

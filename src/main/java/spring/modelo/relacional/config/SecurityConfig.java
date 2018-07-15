package spring.modelo.relacional.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import spring.modelo.relacional.security.JWTAuthenticationFilter;
import spring.modelo.relacional.security.JWTAuthorizationFilter;
import spring.modelo.relacional.security.JWTUtil;

@Configuration
@EnableWebSecurity
// permite cria anotações pré autorizações no endpoint
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// libera acesso para h2-console
	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTUtil jwtUtil;

	// aqui quais os caminhos por padrão vão está liberado
	private static final String[] PUBLIC_MATCHERS = {
			// **- tudo
			"/h2-console/**" };

	// usuario que não está logado só é permitido ler (não pode alterar, excluir e
	// inserir
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**",
			"/estados/**"};

	// para os usuario possam se cadastrar
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes/",
			"/auth/forgot/**"
			};

	// método do WebSecurityConfigurerAdapter
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// ActiveProfiles - pegando os profiles ativos do meu projeto e
		// tiver no test vou querer acessar o h2 e libera o acesso
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		// para o @Bean do cors ser ativado é preciso colocar isso
		// se tiver um CorsConfigurationSource definido será aplicada quando for chamado
		// o http.cors();
		// desabilitar ataque csrf(não precisa se preocupar), pois é no armazenamento de
		// seção
		http.cors().and().csrf().disable();

		// permite autenticação de todas as rotas que está no vetor e o resto é preciso
		// autenticar
		http.authorizeRequests()
				// só metodo get
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
				.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
				// permite tudo aqui
				.antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

		// authenticationManager() - faz parte do WebSecurityConfigurerAdapter
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));

		// filtro de autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

		// para assegurar o back não vai ter seção de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	// aqui é feita a autenticação no framework, vamos ter que informar quem é o
	// userDetailsService que estamos usando
	// e quem é o algoritmo o algoritmo codificação da senha (bcrypt)
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	// configuração básica de cors
	// requisições de multiplas fontes no back seja aceita é
	// precisa informar e é feita nesse método
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		//para aceitar o put e delete
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// applyPermitDefaultValues() - aqui estou dando acesso básico,
		// "/**" - todos os caminhos
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	// criptografar senha!
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

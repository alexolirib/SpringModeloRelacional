package spring.modelo.relacional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringModeloRelacionalApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(SpringModeloRelacionalApplication.class, args);
	}

	// gerar no banco automaticamente
	@Override
	public void run(String... args) throws Exception {

		
	}
}

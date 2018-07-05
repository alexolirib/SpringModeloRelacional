package spring.modelo.relacional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import spring.modelo.relacional.services.S3Service;

@SpringBootApplication
public class SpringModeloRelacionalApplication implements CommandLineRunner {
	
	
	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(SpringModeloRelacionalApplication.class, args);
	}

	// gerar no banco automaticamente
	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("C:\\Users\\adm\\Desktop\\alexandre\\foto-perfil.png");
		
	}
}

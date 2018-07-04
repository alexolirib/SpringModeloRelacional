package spring.modelo.relacional.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.services.Exception.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private EmailService emailService;

	Random random = new Random();

	public void sendNewPassword(String email) {
		// se o cliente existe
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}

		// retorna nova senha aleatória
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		emailService.sendNewPasswordEmail(cliente, newPass);
		clienteRepository.save(cliente);

	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		// usar tabela unicode
		int opt = random.nextInt(3);
		if (opt == 0) {// gera digito
			return (char) (random.nextInt(10) + 48);
		} else {
			if (opt == 1) { // gera letra maiuscula
				return (char) (random.nextInt(26) + 65);
			} else {// gera letra minuscula
				return (char) (random.nextInt(27) + 97);
			}
		}
	}
}

package spring.modelo.relacional.services;

import org.springframework.mail.SimpleMailMessage;

import spring.modelo.relacional.domain.Pedido;

public interface EmailService {
	//envia email de confirmação irá utilizar o outro método
	void sendOrderConfirmationEmail(Pedido obj);
	
	//antes de enviar e ajeita o SimpleMailMessage é precis ajeitar um Pedido
	void sendEmail(SimpleMailMessage msg); 
}

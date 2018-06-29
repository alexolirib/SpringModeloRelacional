package spring.modelo.relacional.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import spring.modelo.relacional.domain.Pedido;

public interface EmailService {
	//--------------------Enviar email normal(Texto plano)----------------------------------
	
	//envia email de confirmação irá utilizar o outro método
	void sendOrderConfirmationEmail(Pedido obj);
	
	//antes de enviar e ajeita o SimpleMailMessage é precis ajeitar um Pedido
	void sendEmail(SimpleMailMessage msg); 
	
	//--------------------Enviar email com html(mesmo métodos sendo html)--------------------
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	//como email vai ser enviado será feito como o html, tem ser - MimeMessage
	void sendHtmlEmail(MimeMessage msg);
}

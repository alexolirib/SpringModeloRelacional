package spring.modelo.relacional.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import spring.modelo.relacional.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	// ele que vai processar o template para forma de string
	@Autowired
	private TemplateEngine templateEngine;

	// para poder instanciar um MimeMessage
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		// sendEmail não está implementado, já pode utilizar mesmo não estando
		// implementado
		// padrão de projeto - template methods
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		// para quem será a mensagem
		sm.setTo(obj.getCliente().getEmail());
		// remetente do email
		sm.setFrom(sender);
		// assunto do email
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}

	// para o email html

	// será responsável por retornar o HTML preenchido com os dados de um pedido
	protected String htmlFromTemplatePedido(Pedido obj) {
		// é preciso desse obj para acesso do template
		Context context = new Context();
		// terá que enviar o obj
		// context.setVariable(apelido do obj no template, esse é o obj);
		context.setVariable("pedido", obj);
		// processar o template , para retornar o html de forma string
		// templateEngine.process(caminho do html, context)
		return templateEngine.process("email/confirmacaoPedido", context);
	}

	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			//se der erro, irei enviar um sem ser o html
			sendOrderConfirmationEmail(obj);
		}
		
	}

	private MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		// para poder instanciar um obj do tipo MimeMessage, precisa injetar um obj do
		// tipo JavaMailSender
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		// para poder atribuir valores na mensagem
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		// mmh.setText(arg0, true = avisando que é um html);
		mmh.setText(htmlFromTemplatePedido(obj), true);

		return mimeMessage;
	}
	
	// para o adm da aplicacao
	@Override
	public void sendOrderConfirmationAdmEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageAdmFromPedido(obj);
		
		sendEmail(sm);
	}
	
	protected SimpleMailMessage prepareSimpleMailMessageAdmFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setFrom(sender);
		sm.setTo(sender);
		sm.setSubject("Novo Pedido Feito! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Parabéns tem um novo pedido!\n\n" + obj.toString()
		+ "\n"+ obj.getEnderecoDeEntrega().toString());
		return sm;
	}
	
}

package spring.modelo.relacional.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.dto.ClienteDTO;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.resource.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	// para pegar o id que vem da uri "/{id}"
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ClienteRepository repo;

	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		// coleção de pares e valores <chave -id, valor - (valor do id) > --
		// (Map<String, String>) - para converter
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); // pega o map da request
		Integer uriId = Integer.parseInt(map.get("id"));

		List<FieldMessage> list = new ArrayList<>();

		Cliente obj = repo.findByEmail(objDto.getEmail());
		
		//fazer isso pois pode ser que passe o mesmo email que já está cadastrado
		if (obj != null) {
			if (!obj.getId().equals(uriId)) {
				list.add(new FieldMessage("email", "Email já cadastrado!"));
			}
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldMessage())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}

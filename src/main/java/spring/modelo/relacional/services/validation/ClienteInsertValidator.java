package spring.modelo.relacional.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import spring.modelo.relacional.domain.Cliente;
import spring.modelo.relacional.domain.enums.TipoCliente;
import spring.modelo.relacional.dto.ClienteNewDto;
import spring.modelo.relacional.repositories.ClienteRepository;
import spring.modelo.relacional.resource.exception.FieldMessage;
import spring.modelo.relacional.services.validation.utils.BR;

//2º passo para criar validação customizada - criada a validação
//pacote de validação - ConstraintValidator<Nome da anotação, Tipo classe que vai aceitar a validação> 
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDto> {

	@Autowired
	private ClienteRepository repo;

	@Override // aqui é se eu quiser botar alguma programação de inicialização
	public void initialize(ClienteInsert ann) {
	}

	@Override // metodo retorna true se for valido e fase se não for, pois vai ficar a lógica
				// de validação
	public boolean isValid(ClienteNewDto objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		// 3º passo programamos a validação
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "Campo cpf Inválido"));
		}

		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "Campo CNPJ Inválido"));
		}

		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já cadastrado"));
		}

		// inclua os testes aqui, inserindo erros na lista(inserndo erro para o spring
		// identificar)
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldMessage())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}

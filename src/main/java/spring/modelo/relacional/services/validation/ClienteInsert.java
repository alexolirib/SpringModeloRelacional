package spring.modelo.relacional.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ClienteInsertValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
//1º passo para criar validação customizada - aqui é criado a anotação
public @interface ClienteInsert {
	String message() default "Erro de validação";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

package spring.modelo.relacional.resource.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import spring.modelo.relacional.services.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	//HttpServletRequest - indormações da requisição
	//objectNotFound(receber as exception, informações da requisição )
	@ExceptionHandler(ObjectNotFoundException.class)//avisar que isso é um tratador de exception
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
				
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}

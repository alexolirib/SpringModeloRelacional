package spring.modelo.relacional.resource.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	
	private static final long serialVersionUID = 1L;

	public ValidationError(Long timeStamp, Integer status, String error, String msg, String path) {
		super(timeStamp, status, error, msg, path);
	}

	private List<FieldMessage> errors = new ArrayList<>();
	
	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}

}

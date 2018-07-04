package spring.modelo.relacional.services.Exception;

//exceção personalizada de autorização para a camada de serviço
public class AuthorizationException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public AuthorizationException(String msg) {
		super(msg);
	}
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

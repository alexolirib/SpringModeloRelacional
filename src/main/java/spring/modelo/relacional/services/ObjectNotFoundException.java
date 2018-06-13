 package spring.modelo.relacional.services;

public class ObjectNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
	//Throwable- outra exceção, seria a causa que aconteceu antes 
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}

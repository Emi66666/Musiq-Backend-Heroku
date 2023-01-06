package hr.fer.drumre.musiq;

public class MusiqException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MusiqException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public MusiqException(String errorMessage) {
		super(errorMessage);
	}
	
}

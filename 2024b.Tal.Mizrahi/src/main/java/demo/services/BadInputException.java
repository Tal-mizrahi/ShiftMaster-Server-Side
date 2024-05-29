package demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadInputException extends RuntimeException {
	private static final long serialVersionUID = -724809990103580059L;

	public BadInputException() {
	}

	public BadInputException(String message) {
		super(message);
	}

	public BadInputException(Throwable cause) {
		super(cause);
	}

	public BadInputException(String message, Throwable cause) {
		super(message, cause);
	}

}

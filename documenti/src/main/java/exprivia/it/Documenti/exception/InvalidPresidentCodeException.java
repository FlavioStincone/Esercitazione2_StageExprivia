package exprivia.it.Documenti.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidPresidentCodeException extends RuntimeException {

    public InvalidPresidentCodeException(String message) {
        super(message);
    }
    
}

package exprivia.it.documenti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DocumentAlreadyExistsException extends RuntimeException {

    public DocumentAlreadyExistsException(String message) {
        super(message);
    }
    
}

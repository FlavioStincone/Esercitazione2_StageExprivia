package exprivia.it.Documenti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmergencyProtocolException extends RuntimeException {

    public EmergencyProtocolException(String message) {
        super(message);
    }
    
}

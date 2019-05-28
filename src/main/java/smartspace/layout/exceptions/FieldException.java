package smartspace.layout.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FieldException extends RuntimeException {
    public FieldException(String serviceName, String field) {
        super(serviceName + ": " + field + " isn't valid");
    }
    public FieldException(String message) {
        super(message);
    }
}

package smartspace.layout;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FieldException extends RuntimeException {
    public FieldException(String serviceName) {
        super(serviceName + ": Not Valid Fields, Validation field");
    }
}

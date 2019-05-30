package smartspace.layout.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IlegalActionType extends RuntimeException {
    public IlegalActionType() {
        super("Ilegal Action Type");
    }
}

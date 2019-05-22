package smartspace.layout;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class RolePermissionException extends RuntimeException {
    public RolePermissionException() {
        super("Not Valid Role User Details");
    }
    public RolePermissionException(String message) {
        super(message);
    }
}

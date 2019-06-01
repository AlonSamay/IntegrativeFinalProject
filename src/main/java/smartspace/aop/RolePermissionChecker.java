package smartspace.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.layout.exceptions.NotFoundException;
import smartspace.layout.exceptions.RolePermissionException;
import smartspace.logic.UserServiceImpl;

import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
public class RolePermissionChecker {

    private UserServiceImpl userService;

    @Autowired
    public RolePermissionChecker(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Around("@annotation(smartspace.aop.RolePermission)")
    public Object userValidation(ProceedingJoinPoint pjp) throws Throwable {
        Object[] methodsArgs = pjp.getArgs();
        String smartSpaceName = String.valueOf(methodsArgs[0]);
        String email = String.valueOf(methodsArgs[1]);
        MethodSignature method = (MethodSignature) pjp.getSignature();
        UserRole[] expectedRoles = method.getMethod().getAnnotation(smartspace.aop.RolePermission.class).value();

        boolean isValid;

        UserEntity userFromDb = this.userService.get(smartSpaceName,email);

        isValid = Arrays.stream(expectedRoles).anyMatch(role -> role == userFromDb.getRole());
        if (!isValid)
            throw new RolePermissionException();

        try {
            Object rv = pjp.proceed();
            return rv;
        } catch (Throwable e) {
            throw e;
        }
    }


}

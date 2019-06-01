package smartspace.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.data.ElementEntity;
import smartspace.data.UserRole;
import smartspace.layout.exceptions.RolePermissionException;
import smartspace.logic.ElementServiceImp;

import java.util.Arrays;

@Component
@Aspect
public class cartAttributesValidator {

    private ElementServiceImp elementService;

    @Autowired
    public cartAttributesValidator(ElementServiceImp elementService) {
        this.elementService = elementService;
    }


    @Around("@annotation(smartspace.aop.RolePermission)")
    public Object cartValidation(ProceedingJoinPoint pjp) throws Throwable {
        Object[] methodsArgs = pjp.getArgs();
        String smartSpaceName = String.valueOf(methodsArgs[0]);
        String email = String.valueOf(methodsArgs[1]);
        MethodSignature method = (MethodSignature) pjp.getSignature();
//        UserRole[] expectedRoles = method.getMethod().getAnnotation(smartspace.aop.ValidateCartAttributes.class).value();

        boolean isValid;

//        ElementEntity cartFromDb = this.elementService.get(smartSpaceName,email);

//        isValid = Arrays.stream(expectedRoles).anyMatch(role -> role == userFromDb.getRole());
//        if (!isValid)
//            throw new RolePermissionException();

        try {
            Object rv = pjp.proceed();
            return rv;
        } catch (Throwable e) {
            throw e;
        }
    }


}

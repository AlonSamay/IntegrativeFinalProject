package smartspace.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.layout.NotFoundException;
import smartspace.layout.RolePermissionException;

import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
public class RolePermissionChecker {

    private EnhancedUserDao userDao;

    @Autowired
    public RolePermissionChecker(EnhancedUserDao userDao) {
        this.userDao = userDao;
    }

//    @Before("@annotation(smartspace.aop.RolePermission) && args(expectedRoles,..)")
//    public void userValidation(JoinPoint pjp ,UserRole[] expectedRoles) throws Throwable {
//
//        Object [] methodsArgs = pjp.getArgs();
//        String smartSpaceName = String.valueOf(methodsArgs[0]);
//        String email = String.valueOf(methodsArgs[1]);
//
//        boolean isValid;
//
//        Optional<UserEntity> userFromDb = this.userDao.readById(new UserKey(email,smartSpaceName));
//
//        isValid = userFromDb.isPresent();
//
//        if(!isValid)
//            throw new NotFoundException("This user ----> " + email +  " Not Found");
//
//        isValid = Arrays.stream(expectedRoles).anyMatch(role-> role == userFromDb.get().getRole());
//        if(!isValid)
//            throw new RolePermissionException();
//
//    }

    @Around("@annotation(smartspace.aop.RolePermission) && args(expectedRoles,..)")
    public Object userValidation(ProceedingJoinPoint pjp, UserRole[] expectedRoles) throws Throwable {

        Object[] methodsArgs = pjp.getArgs();
        String smartSpaceName = String.valueOf(methodsArgs[0]);
        String email = String.valueOf(methodsArgs[1]);

        boolean isValid;

        Optional<UserEntity> userFromDb = this.userDao.readById(new UserKey(email, smartSpaceName));

        isValid = userFromDb.isPresent();

        if (!isValid)
            throw new NotFoundException("This user ----> " + email + " Not Found");

        isValid = Arrays.stream(expectedRoles).anyMatch(role -> role == userFromDb.get().getRole());
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

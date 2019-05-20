package smartspace.aop;

import smartspace.data.UserRole;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface RolePermission {
    UserRole[] value() default {UserRole.MANAGER,UserRole.PLAYER,UserRole.ADMIN};
}

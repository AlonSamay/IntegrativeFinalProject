package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.aop.RolePermission;
import smartspace.data.UserEntity;
import smartspace.logic.UserServiceImpl;

import java.util.Arrays;

import static smartspace.data.UserRole.*;


@RestController
public class UserController implements Controller<UserBoundary> {

    private UserServiceImpl userService;

    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/users/{adminSmartspace}/{adminEmail}";
    private static final String USER_ROUTE = "smartspace/users/login/{userSmartspace}/{userEmail}";
    private static final String ROUTE = "smartspace/users";

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RolePermission(ADMIN)
    @RequestMapping(
            method = RequestMethod.GET,
            path = ADMIN_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return this.userService
                .getAll(size, page)
                .stream()
                .map(UserBoundary::new)
                .toArray(UserBoundary[]::new);
    }

    @RolePermission(ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            path = ADMIN_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody UserBoundary[] userBoundaries) {
        UserEntity[] users = Arrays.stream(userBoundaries)
                .map(UserBoundary::convertToEntity)
                .toArray(UserEntity[]::new);

        return Arrays.stream(this.userService.storeAll(users))
                .map(UserBoundary::new)
                .toArray(UserBoundary[]::new);
    }

    @RolePermission({MANAGER, PLAYER})
    @RequestMapping(
            method = RequestMethod.PUT,
            path = USER_ROUTE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(
            @PathVariable("userSmartspace") String userSmartspace,
            @PathVariable("userEmail") String userEmail,
            @RequestBody UserBoundary userBoundary) {
        userService.update(userBoundary.convertToEntity());
    }

    @RolePermission({MANAGER, PLAYER})
    @RequestMapping(
            method = RequestMethod.GET,
            path = USER_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary get(
            @PathVariable("userSmartspace") String userSmartspace,
            @PathVariable("userEmail") String userEmail) {
        return new UserBoundary(userService.get(userSmartspace, userEmail));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = ROUTE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary create(
            @RequestBody UserForm userForm) {

        return new UserBoundary(userService.store(userForm.convertToEntity()));
    }

}

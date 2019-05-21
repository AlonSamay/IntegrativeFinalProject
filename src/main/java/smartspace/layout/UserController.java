package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.aop.RolePermission;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.logic.UserServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

import static smartspace.data.UserRole.ADMIN;


@RestController
public class UserController implements Controller<UserBoundary> {

    private UserServiceImpl userService;

    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/users/{adminSmartspace}/{adminEmail}";

    @Autowired
    public UserController(EnhancedUserDao userDao, UserServiceImpl userService) {
//        super(userDao);
        this.userService = userService;
    }

    @RolePermission(ADMIN)
    @RequestMapping(
            method= RequestMethod.GET,
            path= ADMIN_ROUTE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
            return this.userService
                    .getAll(size, page)
                    .stream()
                    .map(UserBoundary::new).toArray(UserBoundary[]::new);


    }
    @RolePermission(ADMIN)
    @RequestMapping(
            method=RequestMethod.POST,
            path= ADMIN_ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody UserBoundary[] userBoundaries) {
//            if(this.isAValidUrl(adminEmail,adminSmartSpace)) {
                UserEntity[] users = Arrays.stream(userBoundaries)
                        .map(UserBoundary::convertToEntity)
                        .toArray(UserEntity[]::new);

                return Arrays.stream(this.userService.storeAll(users))
                .map(UserBoundary::new)
                .toArray(UserBoundary[]::new);
    }

}

package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.dao.EnhancedUserDao;
import smartspace.logic.UserServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;


@RestController
public class UserController extends ValidateController implements Controller<UserBoundary> {

    private UserServiceImpl userService;

    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/users/{adminSmartspace}/{adminEmail}";

    @Autowired
    public UserController(EnhancedUserDao userDao, UserServiceImpl userService) {
        super(userDao);
        this.userService = userService;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            path= ADMIN_ROUTE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
        if(this.isAValidUrl(adminEmail,adminSmartSpace))
            return this.userService
                .getAll(size,page)
                .stream()
                .map(UserBoundary::new)
                .collect(Collectors.toList())
                .toArray(new UserBoundary[0]);
        else

            throw new RolePermissionException();

    }

    @RequestMapping(
            method=RequestMethod.POST,
            path= ADMIN_ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody UserBoundary[] userBoundaries) {
            if(this.isAValidUrl(adminEmail,adminSmartSpace))
                return Arrays.stream(userBoundaries)
                        .map(userBoundary -> new UserBoundary(this.userService.store(userBoundary.convertToEntity())))
                        .toArray(UserBoundary[]::new);
            else
                throw new RolePermissionException();
    }

}

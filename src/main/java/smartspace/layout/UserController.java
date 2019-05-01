package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.dao.EnhancedUserDao;
import smartspace.logic.UserService;

import java.util.Arrays;
import java.util.stream.Collectors;

// TODO :
//  handle wrong details
//  sort by for read all

@RestController
public class UserController extends ValidateController implements Controller<UserBoundary> {

    private UserService userService;
    private static final String route  = "users/{adminSmartSpace}/{adminEmail}";

//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
    @Autowired
    public UserController(EnhancedUserDao userDao, UserService userService) {
        super(userDao);
        this.userService = userService;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            path=route,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] getAll(
            @PathVariable("adminSmartSpace") String adminSmartSpace,
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
            throw new RuntimeException("not valid admin details");
    }


    @RequestMapping(
            method=RequestMethod.POST,
            path=route,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] store(
            @PathVariable("adminSmartSpace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody UserBoundary[] userBoundaries) {
            if(this.isAValidUrl(adminEmail,adminSmartSpace))
                return Arrays.stream(userBoundaries)
                        .map(userBoundary -> new UserBoundary(this.userService.store(userBoundary.convertToEntity())))
                        .toArray(UserBoundary[]::new);
            else
                throw new RuntimeException("not valid admin details");
    }

}

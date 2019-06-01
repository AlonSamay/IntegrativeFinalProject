package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.aop.RolePermission;
import smartspace.data.ActionEntity;
import smartspace.data.UserRole;
import smartspace.logic.ActionServiceImpl;
import smartspace.logic.UserServiceImpl;

import java.util.Arrays;

@RestController
public class ActionController extends ValidateController implements Controller<ActionBoundary>{
    private ActionServiceImpl actionService;
    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/actions/{adminSmartspace}/{adminEmail}";
    private static final String ROUTE = "smartspace/actions";

    @Autowired
    public ActionController(UserServiceImpl userService, ActionServiceImpl actionService) {
        super(userService);
        this.actionService = actionService;
    }

    @RolePermission(UserRole.ADMIN)
    @RequestMapping(
            method= RequestMethod.GET,
            path= ADMIN_ROUTE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
                return this.actionService
                        .getAll(size, page)
                        .stream()
                        .map(ActionBoundary::new).toArray(ActionBoundary[]::new);
    }

    @RolePermission(UserRole.ADMIN)
    @RequestMapping(
            method=RequestMethod.POST,
            path= ADMIN_ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody ActionBoundary[] actionBoundaries) {

            ActionEntity[] actions = Arrays.stream(actionBoundaries)
                    .map(ActionBoundary::convertToEntity)
                    .toArray(ActionEntity[]::new);

            return Arrays.stream(this.actionService.storeAll(actions))
                    .map(ActionBoundary::new)
                    .toArray(ActionBoundary[]::new);
    }

//    @RequestMapping(
//            method=RequestMethod.POST,
//            path= ROUTE,
//            produces=MediaType.APPLICATION_JSON_VALUE,
//            consumes=MediaType.APPLICATION_JSON_VALUE)
//    public ActionBoundary invoke(
//            @RequestBody ActionBoundary actionBoundary) {
//        return new ActionBoundary(actionService.store(actionBoundary.convertToEntity()));
//    }

    @RequestMapping(
            method=RequestMethod.POST,
            path= ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary invoke(
            @RequestBody ActionBoundary actionBoundary) {
        return new ActionBoundary(actionService.invoke(actionBoundary.convertToEntity()));
    }





}

package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.dao.EnhancedUserDao;
import smartspace.logic.ActionServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
public class ActionController extends ValidateController implements Controller<ActionBoundary>{
    private ActionServiceImpl actionService;
    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/actions/{adminSmartspace}/{adminEmail}";

    @Autowired
    public ActionController(EnhancedUserDao userDao, ActionServiceImpl actionService) {
        super(userDao);
        this.actionService = actionService;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            path= ADMIN_ROUTE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
            if(this.isAValidUrl(adminEmail,adminSmartSpace))
                return this.actionService
                .getAll(size,page)
                .stream()
                .map(ActionBoundary::new)
                .collect(Collectors.toList())
                .toArray(new ActionBoundary[0]);
            else
                throw new RolePermissionException();
    }

    @RequestMapping(
            method=RequestMethod.POST,
            path= ADMIN_ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody ActionBoundary[] actionBoundaries) {
        if(this.isAValidUrl(adminEmail,adminSmartSpace))
            return Arrays.stream(actionBoundaries)
                    .map(actionBoundary -> new ActionBoundary(this.actionService.store(actionBoundary.convertToEntity()))).toArray(ActionBoundary[]::new);
        else
                throw new RolePermissionException();
    }




}

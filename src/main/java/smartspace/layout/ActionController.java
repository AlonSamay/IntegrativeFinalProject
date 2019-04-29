package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.dao.EnhancedUserDao;
import smartspace.logic.ActionService;

import java.util.Arrays;
import java.util.stream.Collectors;

// TODO :
//  handle wrong details
//  sort by for read all

@RestController
public class ActionController extends ValidateController implements Controller<ActionBoundary>{
    private ActionService actionService;
    private static final String route  = "actions/{adminSmartSpace}/{adminEmail}";

//    @Autowired
//    public ActionController(ActionService actionService) {
//        this.actionService = actionService;
//    }
//
    @Autowired
    public ActionController(EnhancedUserDao userDao, ActionService actionService) {
        super(userDao);
        this.actionService = actionService;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            path=route,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary[] getAll(
            @PathVariable("adminSmartSpace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
        if(this.isAdminRole(adminEmail) && this.isLocalSmartSpace(adminSmartSpace))
            return this.actionService
                .getAll(size,page)
                .stream()
                .map(ActionBoundary::new)
                .collect(Collectors.toList())
                .toArray(new ActionBoundary[0]);
        else
            return new ActionBoundary[0];
    }

    @RequestMapping(
            method=RequestMethod.POST,
            path=route,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ActionBoundary[] store(
            @PathVariable("adminSmartSpace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody ActionBoundary[] actionBoundaries) {
        if(this.isAdminRole(adminEmail) && !this.isLocalSmartSpace(adminSmartSpace))
            return Arrays.stream(actionBoundaries)
                .map(actionBoundary -> new ActionBoundary(this.actionService.store(actionBoundary.convertToEntity())))
                .collect(Collectors.toList())
                .toArray(new ActionBoundary[0]);
        else
            return new ActionBoundary[0];
    }



}

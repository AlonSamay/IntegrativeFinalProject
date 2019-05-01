package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.dao.EnhancedUserDao;
import smartspace.logic.ElementService;

import java.util.Arrays;
import java.util.stream.Collectors;

// TODO :
//  sort by for read all

@RestController
public class ElementController extends ValidateController implements Controller<ElementBoundary> {

    private ElementService elementService;
    private static final String route = "elements/{adminSmartSpace}/{adminEmail}";

    @Autowired
    public ElementController(EnhancedUserDao userDao, ElementService elementService) {
        super(userDao);
        this.elementService = elementService;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            path=route,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] getAll(
            @PathVariable("adminSmartSpace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
        if(this.isAValidUrl(adminEmail,adminSmartSpace))
            return this.elementService
                .getAll(size,page)
                .stream()
                .map(ElementBoundary::new)
                .collect(Collectors.toList())
                .toArray(new ElementBoundary[0]);
        else
            throw new RolePermissionException();
    }

    @RequestMapping(
            method=RequestMethod.POST,
            path=route,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] store(
            @PathVariable("adminSmartSpace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody ElementBoundary[] elementBoundaries) {
        if(this.isAValidUrl(adminEmail,adminSmartSpace))
            return Arrays.stream(elementBoundaries)
                .map(elementBoundary -> new ElementBoundary(this.elementService.store(elementBoundary.convertToEntity())))
                .collect(Collectors.toList())
                .toArray(new ElementBoundary[0]);
        else
            throw new RolePermissionException();
    }

}

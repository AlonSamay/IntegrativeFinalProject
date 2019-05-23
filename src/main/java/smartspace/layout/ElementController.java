package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.aop.RolePermission;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserRole;
import smartspace.logic.ElementServiceImp;

import java.util.Arrays;
import java.util.stream.Collectors;


@RestController
public class ElementController extends ValidateController implements Controller<ElementBoundary> {


    private ElementServiceImp elementService;
    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/elements/{adminSmartspace}/{adminEmail}";
    private static final String MANAGER_ROUTE = "smartspace/elements/{managerSmartSpace}/{managerEmail}";
    private static final String USER_ROUTE = "/smartspace/elements/{userSmartSpace}/{userEmail}";
    private static final String ID = "id";
    private static final String SMARTSPACE = "smartspace";

    @Autowired
    public ElementController(EnhancedUserDao userDao, ElementServiceImp elementService) {
        super(userDao);
        this.elementService = elementService;
    }
    @RolePermission(UserRole.ADMIN)
    @RequestMapping(
            method = RequestMethod.GET,
            path = ADMIN_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminMail,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
            return this.elementService
                    .getAll(size, page)
                    .stream()
                    .map(ElementBoundary::new).toArray(ElementBoundary[]::new);
    }


    @RolePermission(UserRole.ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            path = ADMIN_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
            @PathVariable("adminEmail") String adminEmail,
            @RequestBody ElementBoundary[] elementBoundaries) {

            ElementEntity[] users = Arrays.stream(elementBoundaries)
                    .map(ElementBoundary::convertToEntity)
                    .toArray(ElementEntity[]::new);

            return Arrays.stream(this.elementService.storeAll(users))
                    .map(ElementBoundary::new)
                    .toArray(ElementBoundary[]::new);

    }

    @RolePermission(UserRole.MANAGER)
    @RequestMapping(
            method = RequestMethod.POST,
            path = MANAGER_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary storeByManager(
            @PathVariable("managerSmartSpace") String managerSmartSpace,
            @PathVariable("managerEmail") String managerEmail,
            @RequestBody ElementBoundary elementBoundary) {
        return new ElementBoundary(this.elementService.store(elementBoundary.convertToEntity()));
    }

    @RolePermission(UserRole.MANAGER)
    @RequestMapping(
            method = RequestMethod.PUT,
            path = MANAGER_ROUTE+"/{elementSmartspace}/{elementId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateByManager(
            @PathVariable("managerSmartSpace") String managerSmartSpace,
            @PathVariable("managerEmail") String managerEmail,
            @PathVariable("elementSmartspace") String elementSmartSpace,
            @PathVariable("elementId") String elementId,
            @RequestBody ElementBoundary elementBoundary) {
        setBoundryKeyFromUrl(elementSmartSpace, elementId, elementBoundary);
        this.elementService.update(elementBoundary.convertToEntity());
    }

    private void setBoundryKeyFromUrl(String elementSmartSpace, String elementId, ElementBoundary elementBoundary) {
        ElementKey key = new ElementKey(elementId,elementSmartSpace);
        elementBoundary.setKey(key);
    }

    @RolePermission({UserRole.PLAYER,UserRole.MANAGER})
    @RequestMapping(
            method = RequestMethod.GET,
            path = USER_ROUTE+"/{elementSmartspace}/{elementId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary getElement(
            @PathVariable("userSmartSpace") String userSmartSpace,
            @PathVariable("userEmail") String userEmail,
            @PathVariable("elementSmartspace") String elementSmartSpace,
            @PathVariable("elementId") String elementId) {
        return new ElementBoundary(this.elementService.readById(elementSmartSpace, elementId));
    }


    @RolePermission
    @RequestMapping(
            method = RequestMethod.GET,
            path = USER_ROUTE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] getElementsBySearch(
            @PathVariable("userSmartSpace") String userSmartSpace,
            @PathVariable("userEmail") String userEmail,
            @RequestParam(name = "Search", required = false, defaultValue = "all") String param,
            @RequestParam(name = "value", required = false, defaultValue = "A") String value,
            @RequestParam(name = "x", required = false, defaultValue = "0.0") double x,
            @RequestParam(name = "y", required = false, defaultValue = "0.0") double y,
            @RequestParam(name = "distance", required = false, defaultValue = "0.0") double distance,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        if (isValidInput(param)) {
            return this.elementService
                    .getElementsBySearchTerm(param, value, x, y, distance, size, page)
                    .stream()
                    .map(ElementBoundary::new)
                    .collect(Collectors.toList())
                    .toArray(new ElementBoundary[0]);
        } else {
            throw new RuntimeException("param is invalid");
        }
    }


    private boolean isValidInput(String param) {
        return param.equalsIgnoreCase(Search.ALL.name()) ||
                param.equalsIgnoreCase(Search.LOCATION.name()) ||
                param.equalsIgnoreCase(Search.NAME.name()) ||
                param.equalsIgnoreCase(Search.TYPE.name());
    }


}

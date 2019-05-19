package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.logic.ElementServiceImp;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


@RestController
public class ElementController extends ValidateController implements Controller<ElementBoundary> {

    // TODO  :
    //  1. add not found exception
    //  2. change RolePermissionException
    //  3. AOP
    //  4. use external library to validate the fields
    //  5. change store function of the arrays

    private ElementServiceImp elementService;
    //****************   Commented the context from the proprties file and setted route to each User Role ****************
    private static final String ADMIN_ROUTE = "smartspace/admin/elements/{adminSmartspace}/{adminEmail}";
    private static final String MANAGER_ROUTE = "smartspace/elements/{managerSmartspace}/{managerEmail}";
    private static final String USER_ROUTE = "/smartspace/elements/{userSmartspace}/{userEmail}";
    private static final String ID = "id";
    private static final String SMARTSPACE = "smartspace";

    @Autowired
    public ElementController(EnhancedUserDao userDao, ElementServiceImp elementService) {
        super(userDao);
        this.elementService = elementService;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            path= ADMIN_ROUTE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] getAllByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
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
            path= ADMIN_ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] storeByAdmin(
            @PathVariable("adminSmartspace") String adminSmartSpace,
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


    @RequestMapping(
            method=RequestMethod.POST,
            path = MANAGER_ROUTE,
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary storeByManager(
            @PathVariable("managerSmartSpace") String managerSmartSpace,
            @PathVariable("managerEmail") String managerEmail,
            @RequestBody ElementBoundary elementBoundary) {
            // TODO : add role validation
            return new ElementBoundary(this.elementService.store(elementBoundary.convertToEntity()));
            //return this.elementService.store(elementBoundary.convertToEntity());
    }

    //TODO VALIDATIION
    @RequestMapping(
            method=RequestMethod.PUT,
            path= "smartspace/elements/{managerSmartSpace}/{managerEmail}/{elementSmartspace}/{elementId}",
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
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


    @RequestMapping(
            method=RequestMethod.GET,
            path= "smartspace/elements/{userSmartSpace}/{userEmail}/{elementSmartspace}/{elementId}",
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE)
    //TODO CONVERT RV TO ELEMENT BOUNDRY
    //TODO VALIDATIION
    public ElementEntity getElement(
            @PathVariable("userSmartSpace") String userSmartSpace,
            @PathVariable("userEmail") String userEmail,
            @PathVariable("elementSmartspace") String elementSmartSpace,
            @PathVariable("elementId") String elementId) {
        //TODO throw Not-Found Exception in Service if no user with that id
        return this.elementService.readById(elementSmartSpace, elementId);
    }



    //TODO VALIDATION OF USER
    @RequestMapping(
            method= RequestMethod.GET,
            path= USER_ROUTE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public ElementBoundary[] getElementsBySearch(
            @PathVariable("userSmartspace") String userSmartSpace,
            @PathVariable("userEmail") String userEmail,
            @RequestParam(name="search", required=false, defaultValue="all") String param,
            @RequestParam(name="value", required=false, defaultValue="A") String value,
            @RequestParam(name="x", required=false, defaultValue="0.0") double x,
            @RequestParam(name="y", required=false, defaultValue="0.0") double y,
            @RequestParam(name="distance", required=false, defaultValue="0.0") double distance,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="page", required=false, defaultValue="0") int page) {
        if(isValidInput(param)){
            return this.elementService
                    .getElementsBySearchTerm(param, value, x, y, distance, size,page)
                    .stream()
                    .map(ElementBoundary::new)
                    .collect(Collectors.toList())
                    .toArray(new ElementBoundary[0]);
        }
        else{
            //TODO CHANGE THE EXCEPTION TYPE TO BAD REQUEST
            throw new RuntimeException("param is invalid");
        }
    }


    private boolean isValidInput(String param) {
        return  param.equalsIgnoreCase(search.ALL.name()) ||
                param.equalsIgnoreCase(search.LOCATION.name()) ||
                param.equalsIgnoreCase(search.NAME.name()) ||
                param.equalsIgnoreCase(search.TYPE.name());
    }





}

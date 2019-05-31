package smartspace.layout;

import org.springframework.stereotype.Component;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.logic.UserServiceImpl;


@Component
public class ValidateController {
    private UserServiceImpl userService;

    public ValidateController(UserServiceImpl userDao) {
        this.userService = userDao;
    }

    public boolean isAValidUrl(String email, String smartSpaceName) {
        UserEntity userFromDb = this.userService.get(smartSpaceName,email);
        return userFromDb.getRole() == UserRole.ADMIN && userFromDb.getKey().getSmartspace().equals(smartSpaceName);
    }

//    public boolean isLocalSmartSpace(String smartSpace){
//        return smartSpace != null && smartSpace.equals("2019BTal.Cohen");
//    }

}

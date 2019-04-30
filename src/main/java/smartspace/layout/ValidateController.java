package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

import java.util.Optional;

@Component
public class ValidateController {
    private EnhancedUserDao userDao;

    public ValidateController(EnhancedUserDao userDao) {
        this.userDao = userDao;
    }

    public boolean isAValidUrl (String email,String smartSpaceName) {
        Optional<UserEntity> userFromDb = this.userDao.readById(new UserKey(email));
        if(!userFromDb.isPresent())
            return false;
        else {
            return userFromDb.get().getRole() == UserRole.ADMIN && userFromDb.get().getKey().getId().equals(smartSpaceName);
        }
    }

//    public boolean isLocalSmartSpace(String smartSpace){
//        return smartSpace != null && smartSpace.equals("2019BTal.Cohen");
//    }

}

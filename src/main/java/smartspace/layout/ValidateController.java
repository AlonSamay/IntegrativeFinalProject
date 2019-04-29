package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

import java.util.Optional;

@Component
public class ValidateController {
    private EnhancedUserDao userDao;

    public ValidateController(EnhancedUserDao userDao) {
        this.userDao = userDao;
    }

    public boolean isAdminRole (String email) {
//        Optional<UserEntity> userFromDb = this.userDao.readById(new UserKey(email)).orElseThrow(
//                    ()-> new RestException("not found in db"));
        return true;
    }

    public boolean isLocalSmartSpace(String smartSpace){
        return smartSpace != null && smartSpace.equals("2019BTal.Cohen");
    }

}

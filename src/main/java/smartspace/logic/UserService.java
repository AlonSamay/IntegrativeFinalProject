package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.layout.ValidateController;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService extends Validator implements ServicePattern<UserEntity> {

    private EnhancedUserDao <UserKey>  userDao;

    @Autowired
    public UserService(EnhancedUserDao<UserKey> userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserEntity> getAll(int size, int page) {
        System.out.println("read all user service");
        return this.userDao.readAll(size,page);
    }

    @Override
    @Transactional
    public UserEntity store(UserEntity userEntity) {
        if(this.validate(userEntity))
            return this.userDao.create(userEntity);
        else
            throw  new RuntimeException("input not valid");
    }

    private boolean validate(UserEntity userEntity) {
//        return  validateStringType(userEntity.getKey().getId()) &&
//                validateStringType(userEntity.getKey().getEmail()) &&
//                validateStringType(userEntity.getAvatar()) &&
//                userEntity.getRole() != null &&
//                userEntity.getPoints() instanceof Long &&
//                userEntity.getPoints() > 0;
        return this.isValid(userEntity.getKey().getId()) &&
                this.isValid(userEntity.getKey().getEmail()) &&
                this.isValid(userEntity.getAvatar()) &&
                this.isValid(userEntity.getRole()) &&
                this.isValid(userEntity.getPoints());

    }

//    private boolean validateStringType(String str){
//        return str != null && !str.trim().isEmpty();
//    }
}

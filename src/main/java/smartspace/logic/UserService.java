package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import java.util.List;


@PropertySource("application.properties")
@Service
public class UserService extends Validator implements ServicePattern<UserEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private EnhancedUserDao<UserKey> userDao;

    @Autowired
    public UserService(EnhancedUserDao<UserKey> userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserEntity> getAll(int size, int page) {
        return this.userDao.readAll(size, page);
    }


    @Override
//    @Transactional(rollbackFor = RuntimeException.class,propagation = Propagation.MANDATORY)
    public UserEntity store(UserEntity userEntity) {
        if (this.validate(userEntity)){
            return this.userDao.create(userEntity);
        }
        else
            throw new RuntimeException(this.getClass().getSimpleName());
    }

    private boolean validate(UserEntity userEntity) {

        return this.isValid(userEntity.getKey().getId()) &&
                !userEntity.getKey().getId().equals(this.smartSpaceName) &&
                this.isValid(userEntity.getKey().getEmail()) &&
                this.isValid(userEntity.getAvatar()) &&
                this.isValid(userEntity.getRole()) &&
                this.isValid(userEntity.getPoints()) &&
                this.isValid(userEntity.getUsername());
    }

}

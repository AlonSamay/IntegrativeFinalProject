package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.MailAdress;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@PropertySource("application.properties")
@Service
public class UserServiceImpl extends Validator implements UserService<UserEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private EnhancedUserDao<UserKey> userDao;

    @Autowired
    public UserServiceImpl(EnhancedUserDao<UserKey> userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserEntity> getAll(int size, int page) {
        return this.userDao.readAll(size, page);
    }


    @Override
    @Transactional
    public UserEntity store(UserEntity userEntity) {

        if (this.validate(userEntity)){
            return this.userDao.create(userEntity);
        }
        else
            throw new RuntimeException(this.getClass().getSimpleName());
    }

    @Transactional
    public UserEntity[] storeAll(UserEntity[] userEntities) {
        boolean isAllValid=Arrays.stream(userEntities).allMatch(this::validate);
        if (isAllValid){
            return Arrays.stream(userEntities).map(this::store).collect(Collectors.toList()).toArray(new UserEntity[0]);
        }
        else
            throw new RuntimeException(this.getClass().getSimpleName());
    }

    private boolean validate(UserEntity userEntity) {

        return this.isValid(userEntity.getKey().getId()) &&
                !userEntity.getKey().getId().equals(this.smartSpaceName) &&
                this.isValid(new MailAdress(userEntity.getKey().getEmail())) &&
                this.isValid(userEntity.getAvatar()) &&
                this.isValid(userEntity.getRole()) &&
                this.isValid(userEntity.getPoints()) &&
                this.isValid(userEntity.getUsername());
    }

}

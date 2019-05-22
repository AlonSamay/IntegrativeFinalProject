package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.EmailAddress;
import smartspace.data.MailAdress;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


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
        if (!this.validate(userEntity)) {
            throw new RuntimeException(this.getClass().getSimpleName() + ": Invalid entity");
        }
        return this.userDao.create(userEntity);
    }

    @Override
    @Transactional
    public UserEntity get(String smartspace, String email) {
        UserKey userKey = new UserKey(email, smartspace);
        Optional<UserEntity> entityFromDB = userDao.readById(userKey);
        if (!entityFromDB.isPresent()) {
            throw new RuntimeException(this.getClass().getSimpleName() + ": No user with email and smartspace provided");
        }
        return entityFromDB.get();
    }

    @Override
    @Transactional
    public UserEntity[] storeAll(UserEntity[] userEntities) {
        boolean isAllValid = Arrays.stream(userEntities)
                .allMatch(this::validateDifferentSmartspace);
        if (!isAllValid) {
            throw new RuntimeException(this.getClass().getSimpleName());
        }
        return Arrays.stream(userEntities)
                .map(this::store)
                .toArray(UserEntity[]::new);
    }

    @Override
    @Transactional
    public void update(UserEntity entity) {
        if (!validate(entity)) {
            throw new RuntimeException(this.getClass().getSimpleName());
        }
        Optional<UserEntity> entityFromDB = userDao.readById(entity.getKey());
        if (!entityFromDB.isPresent()) {
            throw new RuntimeException(this.getClass().getSimpleName());
        }
        entity.setPoints(entityFromDB.get().getPoints());

        userDao.update(entity);
    }

    private boolean validateDifferentSmartspace(UserEntity entity) {
         return validate(entity)
                 && !entity.getUserKey().getSmartspace().equals(this.smartSpaceName);
    }

    private boolean validate(UserEntity userEntity) {

        return this.isValid(userEntity.getKey().getSmartspace()) &&
                !userEntity.getKey().getSmartspace().equals(this.smartSpaceName) &&
                this.isValid(userEntity.getKey().getEmail()) &&
                this.isValid(userEntity.getAvatar()) &&
                this.isValid(userEntity.getRole()) &&
                this.isValid(userEntity.getPoints()) &&
                this.isValid(userEntity.getUsername());
    }
}

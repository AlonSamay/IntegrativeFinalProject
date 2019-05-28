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
import smartspace.layout.exceptions.FieldException;
import smartspace.layout.exceptions.NotFoundException;

import java.lang.reflect.Field;
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
        this.validate(userEntity);
        return this.userDao.create(userEntity);
    }

    @Override
    @Transactional
    public UserEntity get(String smartspace, String email) {
        UserKey userKey = new UserKey(email, smartspace);
        return getUser(userKey);
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
        validate(entity);
        UserEntity entityFromDB = getUser(entity.getKey());
        entity.setPoints(entityFromDB.getPoints());
        userDao.update(entity);
    }

    private boolean validateDifferentSmartspace(UserEntity entity) {
        return !entity.getUserKey().getSmartspace().equals(this.smartSpaceName);
    }

    private UserEntity getUser(UserKey key) {
        Optional<UserEntity> entityFromDB = userDao.readById(key);
        if (!entityFromDB.isPresent())
            throw new NotFoundException(this.getClass().getSimpleName() + ": No such user with specified userKey");
        return entityFromDB.get();

    }

    private void validate(UserEntity userEntity) {
        if (userEntity.getKey() == null)
            throw new FieldException(this.getClass().getSimpleName(), "User's key");

        if (!this.isValid(new EmailAddress(userEntity.getKey().getEmail())))
            throw new FieldException(this.getClass().getSimpleName(), "User's email");

        if (!this.isValid(userEntity.getKey().getSmartspace()))
            throw new FieldException(this.getClass().getSimpleName(), "User's smartspace");

        if (!this.isValid(userEntity.getRole()))
            throw new FieldException(this.getClass().getSimpleName(), "User's role");

        if (!this.isValid(userEntity.getAvatar()))
            throw new FieldException(this.getClass().getSimpleName(), "User's avatar");

        if (!this.isValid(userEntity.getPoints()))
            throw new FieldException(this.getClass().getSimpleName(), "User's points");

        if (!this.isValid(userEntity.getUsername()))
            throw new FieldException(this.getClass().getSimpleName(), "User's name");

//        return this.isValid(userEntity.getKey().getSmartspace()) &&
//                userEntity.getKey().getSmartspace().equals(this.smartSpaceName) &&
//                this.isValid(userEntity.getKey().getEmail()) &&
//                this.isValid(userEntity.getAvatar()) &&
//                this.isValid(userEntity.getRole()) &&
//                this.isValid(userEntity.getPoints()) &&
//                this.isValid(userEntity.getUsername());
    }
}

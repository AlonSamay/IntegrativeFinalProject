package smartspace.dao.nonrdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;


@Repository
public class nonRdbUserDao implements EnhancedUserDao<UserKey> {
    private NUserCrud userCrud;
    private final String USER_NAME = "username";


    @Autowired
    public nonRdbUserDao(NUserCrud userCrud) {
        this.userCrud = userCrud;
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        return this.userCrud.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> readAll() {
        List<UserEntity> rv = new ArrayList<>();
        this.userCrud
                .findAll()
                .forEach(user -> rv.add(user));
        return rv;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> readById(UserKey key) {
        return this.userCrud.findById(key);
    }

    @Override
    @Transactional
    public void update(UserEntity update) {
        if (this.userCrud.existsById(update.getKey())) {
            this.userCrud.save(update);
        } else {
            throw new RuntimeException("nonRdbUserDao: no user with id: " + update.getKey());
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        this.userCrud.deleteAll();
    }


    @Override
    public List<UserEntity> readAll(int size, int page) {
        return userCrud.findAll(
                PageRequest.of(
                        page,
                        size,
                        ASC,
                        USER_NAME))
                .getContent();
    }


}

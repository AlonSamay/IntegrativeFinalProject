package smartspace.dao;

import smartspace.data.UserEntity;

import java.util.List;

public interface EnhancedUserDao<k> extends UserDao<k>{
    List<UserEntity> readAll (int size, int page);

}

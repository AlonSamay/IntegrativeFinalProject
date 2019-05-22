package smartspace.logic;

import smartspace.data.UserEntity;

public interface UserService<K> extends ServicePattern<K>{
    K get(String smartspace, String email);
    K[] storeAll(K[] entity);
    void update(K entity);
}

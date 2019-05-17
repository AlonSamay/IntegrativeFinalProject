package smartspace.dao;

import smartspace.data.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserDao<K> {

		UserEntity create(UserEntity user);

		//SELECT - Read

		List<UserEntity> readAll();
		Optional<UserEntity> readById(K key);

		//UPDATE - Update

		void update(UserEntity update);

		//DELETE - Delete
		void deleteAll();
}

package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import smartspace.dao.IdGenerator;
import smartspace.dao.IdGeneratorCrud;
import smartspace.dao.UserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Repository
public class RdbUserDao implements UserDao<UserKey> {
	private UserCrud userCrud;
	private IdGeneratorCrud idGeneratorCrud;
	
	
	@Autowired
	public RdbUserDao(UserCrud userCrud, IdGeneratorCrud idGeneratorCrud) {
		this.userCrud = userCrud;
		this.idGeneratorCrud = idGeneratorCrud;
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity user) {
		IdGenerator idGenerator = new IdGenerator();
		user.setKey(new UserKey(this.idGeneratorCrud.save(idGenerator).getNextId()));
		return this.userCrud.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll() {
		List<UserEntity> rv = new ArrayList<>();
		this.userCrud
		.findAll()
		.forEach(user->rv.add(user));
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
		if(this.userCrud.existsById(update.getKey())) {
			this.userCrud.save(update);
		}else {
			throw new RuntimeException("no user with id: " + update.getKey());
		}
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.userCrud.deleteAll();
	}
}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.Application;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {
		"spring.profiles.active=default"})
public class UserDaoTests {
	private RdbUserDao userDao;
	private EntityFactoryImpl factory;
	
	@Autowired
	public void setMessageDao(RdbUserDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setFactory(EntityFactoryImpl factory) {
		this.factory = factory;
	}
	
	@Before
	public void setup() {
		this.userDao.deleteAll();
	}

	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testCreateOneUser(){
		// GIVEN the database is clean

		// WHEN I insert new user to the database
		UserEntity user1 =
				this.factory.createNewUser("yanai1@gmail.com", "Yanai", "Yanai100", "1112", UserRole.PLAYER, (long) 123);
		this.userDao.create(user1);

		// THEN the database contains a user with the right details
		assertThat(this.userDao.readAll()).usingElementComparatorOnFields("userEmail","userSmartSpace","username", "avatar", "role", "points", "userKey").contains(user1);
	}
	
	@Test
	public void testCreateUsers(){
		// GIVEN the database is clean
		
		// WHEN I insert 2 users to the database
		UserEntity user1 =
			this.factory.createNewUser("yanai1@gmail.com", "Yanai", "Yanai100", "1112", UserRole.PLAYER, (long) 123);
		this.userDao.create(user1);
		UserEntity user2 =
				this.factory.createNewUser("Alon1@gmail.com", "Alon", "Alon100", "1113", UserRole.PLAYER, (long) 124);
		this.userDao.create(user2);
		
		// THEN the database contains those two users with the right details
		//assertThat(user1.getUsername()).isEqualTo(this.userDao.readAll().get(0).getUsername());
		assertThat(this.userDao.readAll()).usingElementComparatorOnFields("userEmail","userSmartSpace","username", "avatar", "role", "points", "userKey").contains(user1,user2);
	}


	@Test
	public void testCreateManyUser(){
		// GIVEN the database contains 8 users
		List<UserEntity> newUsers = createUsers(8);

		// WHEN we read all the users from the database
		List<UserEntity> actual = this.userDao.readAll();

		// THEN we receive 8 users exactly
		assertThat(actual)
				.hasSize(8);
		assertThat(actual)
				.usingElementComparatorOnFields("userEmail","userSmartSpace","username", "avatar", "role", "points", "userKey")
				.containsExactlyElementsOf(
						new ArrayList<>(newUsers));

	}


	private List<UserEntity> createUsers(int size) {
		return IntStream.range(1, size + 1)
				.mapToObj(i -> this.factory.createNewUser(""+i, ""+i, ""+i, ""+i, UserRole.PLAYER, (long) i)).map(this.userDao::create)
				.collect(Collectors.toList());
	}


}

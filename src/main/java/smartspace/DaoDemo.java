package smartspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.IdGeneratorCrud;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class DaoDemo implements CommandLineRunner{
	private EnhancedElementDao<ElementKey> enchancedElementDao;
	private EnhancedActionDao enhancedActionDao;
	private EnhancedUserDao<UserKey> enhancedUserDao;
	private IdGeneratorCrud idGeneratorCrud;
	private EntityFactory factory;


	@Autowired
	public DaoDemo(EnhancedUserDao<UserKey> enhancedUserDao,
				   EnhancedActionDao enhancedActionDao,
				   EnhancedElementDao<ElementKey> enchancedElementDao,
				   IdGeneratorCrud idGeneratorCrud,
				   EntityFactory factory) {
		super();
		this.enhancedUserDao = enhancedUserDao;
		this.enhancedActionDao = enhancedActionDao;
		this.enchancedElementDao = enchancedElementDao;
		this.idGeneratorCrud = idGeneratorCrud;
		this.factory = factory;

	}


	@Override
	public void run(String... args) throws Exception {

		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("x", 5);
		moreAttributes.put("y", 42);


		createUser();
		createElement(moreAttributes);
		createAction(moreAttributes);


//		readAllUsers();
//		readAllElements();
//		readAllActions();

		String name = "B";
		readAllByName(name);

		String type = "Login";
		readAllByType(type);

//		ElementEntity elementEntity = readElementById();
//		UserEntity userEntity = readUserById();


//
//		updateUser(userEntity);
//		updateEntity(elementEntity);


//		enchancedElementDao.deleteByKey(new ElementKey("5cc17077b03b0a6225bf7088"));
//		readAllElements();


//		elementDao.delete(elementEntity);
//		readAllElements();
//
//        enchancedElementDao.deleteAll();
//        enhancedActionDao.deleteAll();
//        enhancedUserDao.deleteAll();
	}

	private void readAllByType(String type) {
		enchancedElementDao.readAllByType(type,4,0)
				.forEach(element->System.err.println(element));
	}

	private void readAllByName(String name) {
		enchancedElementDao.readAllByName(name,4,0)
							.forEach(element->System.err.println(element));
	}

	private void readAllActions() {
		enhancedActionDao
				.readAll(10, 0 ) // list
				.forEach(action->System.err.println(action));
	}

	private void readAllUsers() {
		enhancedUserDao.readAll(10,0)// list
		.forEach(user->System.err.println(user));
	}

	private void readAllElements() {
		enchancedElementDao
				.readAll(10, 0 ) // list
				.forEach(element -> System.err.println(element));
	}

	private void updateEntity(ElementEntity elementEntity) {
		elementEntity.setCreatorEmail("Test33");
		enchancedElementDao.update(elementEntity);
		System.err.print("After " + enchancedElementDao.readById(elementEntity.getKey()).get().toString());
	}

	private void updateUser(UserEntity userEntity) {
		userEntity.setUsername("TEST33");
		enhancedUserDao.update(userEntity);
		System.err.print("After " + enhancedUserDao.readById(userEntity.getKey()));
	}

	private UserEntity readUserById() {
		UserEntity userEntity= enhancedUserDao.readById(new UserKey("a@gmail.com")).get();
		System.err.print(userEntity);
		return userEntity;
	}

	private ElementEntity readElementById() {
		ElementEntity elementEntity = enchancedElementDao.readById(new ElementKey("5cc17077b03b0a6225bf7088","2019BTalCohen")).get();
		System.err.print(elementEntity);
		return elementEntity;
	}

	private void createAction(Map<String, Object> moreAttributes) {
		ActionEntity action = factory.createNewAction(
					"1",
					"Chat",
					"Delete",
					new Date(),
					"c@gmail.com",
					"Me",
					moreAttributes);
		this.enhancedActionDao.create(action);
	}

	private void createElement(Map<String, Object> moreAttributes) {
		ElementEntity element = factory.createNewElement(
					"AmazonCatalog",
					"Catalog",
					 new Location(1.0, 2.0),
					new Date(),
					"b@gmail.com",
					"y",
					true,
					moreAttributes);
		ElementKey elementKey = new ElementKey("my_smartspace", "30");
		element.setElementKey(elementKey);
		this.enchancedElementDao.create(element);

	}

	private void createUser() {
//		UserEntity user= factory.createNewUser(
//				"AlonSamay@gmail.com",
//				"TalCohen2019.B",
//				"AlonSamay",
//				"C",
//		         UserRole.PLAYER,
//				(long) 123
//		);
//		this.enhancedUserDao.create(user);

		UserEntity user1 = factory.createNewUser(
				"Alon@gmail.com",
				"TalCohen2019.B",
				"AlonSamay",
				":)",
				UserRole.MANAGER,
				(long) 456);
		this.enhancedUserDao.create(user1);



		UserEntity user2 = factory.createNewUser(
				"alon@gmail.com",
				"TalCohen2019.B",
				"OrenShadmi",
				"1234",
				UserRole.ADMIN,
				(long) 123);
		this.enhancedUserDao.create(user2);


	}

}

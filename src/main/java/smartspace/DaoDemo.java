package smartspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import smartspace.dao.*;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;

import java.util.Arrays;
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

	@Value("${SmartSpace.name.property}")
	private String smartSpaceName;
	private String managerMail;
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
		this.managerMail="Alon@gmail.com";
	}


	@Override
	public void run(String... args) throws Exception {

		createUsers();
		createElements();
		createActions();


//		readAllUsers();
//		readAllElements();
//		readAllActions();

		String name = "cart";
		readAllByName(name);

		String type = "Product";
		readAllByType(type);

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

	private void createActions() {

		ActionEntity likeProduct1 = factory.createNewAction(
				"2",
				this.smartSpaceName,
				"like",
				new Date(),
				"talcohen19@gmail.com",
				smartSpaceName,
				new HashMap<>());
		this.enhancedActionDao.create(likeProduct1);

		ActionEntity likeProduct2 = factory.createNewAction(
				"3",
				this.smartSpaceName,
				"like",
				new Date(),
				"talcohen19@gmail.com",
				smartSpaceName,
				new HashMap<>());
		this.enhancedActionDao.create(likeProduct2);


		HashMap<String,Object> moreAtt = new HashMap<>();
		moreAtt.put("Products",Arrays.toString(new String[]{"2,3"}));
		moreAtt.put("Amount","57");
		ActionEntity addToCart = factory.createNewAction(
				"5",
				this.smartSpaceName,
				"addToCart",
				new Date(),
				"talcohen19@gmail.com",
				this.smartSpaceName,moreAtt);
		this.enhancedActionDao.create(addToCart);

	}

	private void createElements() {
		HashMap<String,Object> moreAtt=new HashMap<>();
		moreAtt.put("Products", Arrays.toString(new String[]{"2", "3", "4"}));
		ElementEntity catalog = factory.createNewElement(
					"AmazonCatalog",
					"Catalog",
					 new Location(1.0, 2.0),
					new Date(),
				managerMail,
					this.smartSpaceName,
					false,
				moreAtt);
		ElementKey catalogKey = new ElementKey("1",this.smartSpaceName);
		catalog.setElementKey(catalogKey);
		this.enchancedElementDao.create(catalog);

		moreAtt.clear();

		moreAtt.put("size","M");
		moreAtt.put("color","blue");
		moreAtt.put("price","26");
		moreAtt.put("description","long shirt");
		ElementEntity product1 = factory.createNewElement(
				"shirtMango",
				"product",
				new Location(1.0, 1.0),
				new Date(),
				managerMail,
				this.smartSpaceName,
				false,
				moreAtt);
		ElementKey product1Key = new ElementKey("2",this.smartSpaceName);
		product1.setElementKey(product1Key);
		this.enchancedElementDao.create(product1);

		moreAtt.clear();


		moreAtt.put("size","L");
		moreAtt.put("color","white");
		moreAtt.put("price","24");
		moreAtt.put("description","jeans");
		ElementEntity product2 = factory.createNewElement(
				"LevisJeans",
				"product",
				new Location(1.0, 2.0),
				new Date(),
				managerMail,
				this.smartSpaceName,
				false,
				moreAtt);
		ElementKey product2Key = new ElementKey("3",this.smartSpaceName);
		product2.setElementKey(product2Key);
		this.enchancedElementDao.create(product2);

		moreAtt.clear();

		moreAtt.put("size","S");
		moreAtt.put("color","black");
		moreAtt.put("price","33");
		moreAtt.put("description","elegent shirt");
		ElementEntity product3 = factory.createNewElement(
				"CastroShirt",
				"product",
				new Location(2.0, 1.0),
				new Date(),
				managerMail,
				this.smartSpaceName,
				false,
				moreAtt);
		ElementKey product3Key = new ElementKey("4",this.smartSpaceName);
		product3.setElementKey(product3Key);
		this.enchancedElementDao.create(product3);

		moreAtt.clear();

		ElementEntity cartGenerator = factory.createNewElement(
				"cartGenerator",
				"cartGenerator",
				new Location(0.0, 0.0),
				new Date(),
				managerMail,
				this.smartSpaceName,
				false,
				moreAtt);
		ElementKey cartGeneratorKey = new ElementKey("5",this.smartSpaceName);
		cartGenerator.setElementKey(cartGeneratorKey);
		this.enchancedElementDao.create(cartGenerator);

		moreAtt.clear();

		moreAtt.put("PlayerMail","talcohen19@gmail.com");
		moreAtt.put("PlayerSmartSpace",this.smartSpaceName);
		ElementEntity cart = factory.createNewElement(
				"carttalcohen19@gmail.com",
				"cart",
				new Location(3.0,3.0),
				new Date(),
				this.managerMail,
				this.smartSpaceName,
				false,
				moreAtt);

		ElementKey cartKey = new ElementKey("6",this.smartSpaceName);
		cart.setElementKey(cartKey);
		this.enchancedElementDao.create(cart);

		moreAtt.clear();

		moreAtt.put("PlayerMail","tomrozanski@gmail.com");
		moreAtt.put("PlayerSmartSpace",this.smartSpaceName);
		ElementEntity cart2 = factory.createNewElement(
				"carttomrozanski@gmail.com",
				"cart",
				new Location(4.0,4.0),
				new Date(),
				this.managerMail,
				this.smartSpaceName,
				false,
				moreAtt);

		ElementKey cart2Key = new ElementKey("7",this.smartSpaceName);
		cart2.setElementKey(cart2Key);
		this.enchancedElementDao.create(cart2);


	}

	private void createUsers() {
		UserEntity player1= factory.createNewUser(
				"talCohen@gmail.com",
				this.smartSpaceName,
				"talCohen",
				"!TAL!",
		         UserRole.PLAYER,
				(long) 123
		);
		this.enhancedUserDao.create(player1);

		UserEntity player2= factory.createNewUser(
				"tomrozanski@gmail.com",
				this.smartSpaceName,
				"tomRozanski",
				"@TOM@",
				UserRole.PLAYER,
				(long) 123
		);
		this.enhancedUserDao.create(player2);

		UserEntity manager = factory.createNewUser(
				this.managerMail,
				this.smartSpaceName,
				"AlonSamay",
				":)",
				UserRole.MANAGER,
				(long) 456);
		this.enhancedUserDao.create(manager);



		UserEntity admin = factory.createNewUser(
				"orenShadmi@gmail.com",
				this.smartSpaceName,
				"OrenShadmi",
				"-OREN-",
				UserRole.ADMIN,
				(long) 123);
		this.enhancedUserDao.create(admin);


	}

}

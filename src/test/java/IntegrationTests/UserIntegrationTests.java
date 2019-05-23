package IntegrationTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import smartspace.Application;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserForm;
import smartspace.logic.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class UserIntegrationTests {
    private static final String ADMIN_SMARTSPACE = "2019BTal.Cohen";
    private static final String ADMIN_EMAIL = "alon@gmail.com";

    private String baseURL;
    private String adminURL;
    private String userURL;
    private int port;

    private EntityFactory factory;
    private EnhancedUserDao<UserKey> userDao;
    private UserServiceImpl userService;
    private RestTemplate restTemplate;

    private static int counter = 0;

    @Autowired
    public void setUserDao(EnhancedUserDao<UserKey> userDao) {
        this.userDao = userDao;
    }

    @LocalServerPort
    public void setPort(int port) {
        this.port = port;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void init() {
        this.baseURL = "http://localhost:" + port + "/smartspace";
        this.adminURL ="/admin/users/{adminSmartspace}/{adminEmail}";
        this.userURL ="/users/login/{userSmartspace}/{userEmail}";

        this.restTemplate = new RestTemplate();
    }

    @Before
    public void createAdmin() {
        this.userDao.deleteAll();

        UserEntity admin = factory.createNewUser(
                ADMIN_EMAIL, ADMIN_SMARTSPACE,
                "AlonSamay",
                ":)",
                UserRole.ADMIN,
                (long) 456);
        this.userDao.create(admin);
    }

    public UserKey generateUserKey() {
        UserKey key = new UserKey();
        key.setSmartspace("Bla" + (++counter));
        key.setEmail("bla@gmail.com");
        return key;
    }

    @After
    public void tearDown() {
        this.userDao.deleteAll();
    }

    @Test
    public void testUsersPostRequest() {
        // GIVEN nothing

        // WHEN 10 user boundaries are posted to the server
        int totalSize = 10;

        List<UserBoundary> allUsers =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewUser(
                                String.format("user#%d@gmail.com", i),
                                "2019BTal.Cohen",
                                "user #" + i,
                                ":)",
                                UserRole.PLAYER,
                                (long) 200))
                        .peek(user -> user.setKey(generateUserKey()))
                        .map(UserBoundary::new)
                        .collect(Collectors.toList());

        List<UserEntity> actualResult =
                Arrays.stream(Objects.requireNonNull(
                        this.restTemplate.postForObject(
                                this.adminURL,
                                allUsers,
                                UserBoundary[].class,
                                ADMIN_SMARTSPACE,
                                ADMIN_EMAIL)))
                        .map(UserBoundary::convertToEntity)
                        .collect(Collectors.toList());

        // THEN the database contains the new users
        for (int i = 0; i < totalSize; i++) {
            assertThat(this.userDao.readById(actualResult.get(i).getKey()))
                    .isPresent()
                    .get()
                    .extracting("key", "username", "role")
                    .containsExactly(actualResult.get(i).getKey(), actualResult.get(i).getUsername(), UserRole.PLAYER);
        }
    }

    @Test
    public void testGetAllUsingPagination() {
        // GIVEN the database contains 10 messages
        int totalSize = 10;

        List<UserBoundary> allUsers =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewUser(
                                String.format("user#%d@gmail.com", i),
                                "2019BTal.Cohen",
                                "user #" + i,
                                ":)",
                                UserRole.PLAYER,
                                (long) 200))
                        .peek(user -> user.setKey(generateUserKey()))
                        .map(this.userService::store)
                        .map(UserBoundary::new)
                        .collect(Collectors.toList());

        List<UserBoundary> expected =
                allUsers
                        .stream()
                        .skip(3)
                        .limit(5)
                        .collect(Collectors.toList());

        // WHEN I get all users using page 1 and size 5
        int page = 1;
        int size = 5;
        UserBoundary[] result =
                this.restTemplate
                        .getForObject(
                                this.adminURL + "?size={size}&page={page}",
                                UserBoundary[].class,
                                ADMIN_SMARTSPACE,
                                ADMIN_EMAIL,
                                size,
                                page);

        // THEN the result contains 5 users of the users inserted to the database
        assertThat(result)
                .usingElementComparatorOnFields("key")
                .containsExactlyElementsOf(expected);
    }

    @Test(expected = Exception.class)
    public void testGetUsersWithInvalidRole() {
        // GIVEN nothing

        // WHEN I invoke GET of users by the role of "TEMP"
        String type = "TEMP";
        this.restTemplate
                .getForObject(
                        this.adminURL + "/{type}",
                        UserBoundary[].class,
                        type);

        // THEN I receive an error status
    }

    @Test
    public void testNewUser() {
        // GIVEN nothing

        // WHEN I create new user using user form
        UserForm userForm = new UserForm("samay.alon@gmail.com",
                "PLAYER",
                "AloNs",
                "T_T");

        UserBoundary boundary = this.restTemplate.postForObject(this.baseURL + "/users", userForm, UserBoundary.class);

        // THEN the new user is added to the DB
        assertThat(boundary)
                .isEqualToComparingOnlyGivenFields(userForm, "role", "username", "avatar");
    }

    @Test
    public void testUpdateExistingUser() {
        // GIVEN a user in the database
        UserEntity user = factory.createNewUser(
                "alon@gmail.com",
                "kk",
                "AlonSamay",
                ":)",
                UserRole.PLAYER,
                (long) 100);
        this.userDao.create(user);

        // WHEN I update the username, avatar and points
        user.setUsername("AloNs");
        user.setAvatar(":/");
        user.setPoints((long) 200);

        restTemplate.put(baseURL + userURL, new UserBoundary(user), "kk", "alon@gmail.com");

        // THEN all attributes with changes (without points) will update
        Optional<UserEntity> entity = userDao.readById(user.getKey());
        if (!entity.isPresent()) {
            throw new RuntimeException(this.getClass().getSimpleName());
        }
        UserEntity userEntity = entity.get();
        assertThat(user)
                .isEqualToIgnoringGivenFields(userEntity, "userKey", "username", "avatar");
    }

    @Test
    public void testGetUser() {
        // GIVEN a user in the database
        UserEntity user = factory.createNewUser(
                "yanai1000@gmail.com",
                "jj",
                "Yanai",
                ":S",
                UserRole.PLAYER,
                (long) 100);
        userDao.create(user);

        // WHEN I invoke get request of user
        UserBoundary boundary = restTemplate.getForObject(baseURL + userURL, UserBoundary.class, "jj", "yanai1000@gmail.com");
        UserEntity entity = boundary != null ? boundary.convertToEntity() : null;

        // THEN I get the exact user
        assertThat(user).isEqualToIgnoringGivenFields(entity, "userKey");
    }
}

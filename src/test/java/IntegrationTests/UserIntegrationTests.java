package IntegrationTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Assert;
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
import smartspace.logic.UserService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class UserIntegrationTests {
    private static final String ADMIN_SMARTSPACE = "2019BTal.Cohen";
    private static final String ADMIN_EMAIL = "alon@gmail.com";

    private EntityFactory factory;
    private String baseUrl;
    private int port;
    private EnhancedUserDao<UserKey> userDao;
    private UserService userService;
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
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void init() {
        this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users/{adminSmartSpace}/{ADMIN_EMAIL}";
        this.restTemplate = new RestTemplate();
    }

    @Before
    public void createAdmin() {
        this.userDao.deleteAll();

        UserEntity user1 = factory.createNewUser(
                "AlonSamay",
                ":)",
                UserRole.ADMIN,
                (long) 456);
        user1.setKey(new UserKey("alon@gmail.com"));
        this.userDao.create(user1);
    }

    public UserKey generateUserKey() {
        UserKey key = new UserKey();
        key.setId("Bla" + (++counter));
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
                                this.baseUrl,
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
                                this.baseUrl + "?size={size}&page={page}",
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
                        this.baseUrl + "/{type}",
                        UserBoundary[].class,
                        type);

        // THEN I receive an error status
    }
}

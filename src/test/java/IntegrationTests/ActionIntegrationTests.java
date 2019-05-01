package IntegrationTests;

import static org.assertj.core.api.Assertions.assertThat;

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
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.UserDao;
import smartspace.dao.rdb.RdbActionDao;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserKey;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.layout.UserBoundary;
import smartspace.logic.ActionService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class ActionIntegrationTests {
    private static final String ADMIN_SMARTSPACE = "2019BTal.Cohen";
    private static final String ADMIN_EMAIL = "alon@gmail.com";

    private EntityFactory factory;
    private String baseUrl;
    private int port;
    private EnhancedActionDao actionDao;
    private ActionService actionService;
    private RestTemplate restTemplate;

    private static int counter = 0;

    @Autowired
    public void setUserDao(EnhancedActionDao actionDao) {
        this.actionDao = actionDao;
    }

    @LocalServerPort
    public void setPort(int port) {
        this.port = port;
    }

    @Autowired
    public void setUserService(ActionService actionService) {
        this.actionService = actionService;
    }

    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void init() {
        this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions/{adminSmartSpace}/{ADMIN_EMAIL}";
        this.restTemplate = new RestTemplate();
    }

    public UserKey generateUserKey() {
        UserKey key = new UserKey();
        key.setId("Bla" + (++counter));
        key.setEmail("bla@gmail.com");
        return key;
    }

    @After
    public void tearDown() {
        this.actionDao.deleteAll();
    }

    @Test
    public void testUsersPostRequest() {
        // GIVEN nothing

        // WHEN 10 action boundaries are posted to the server
        int totalSize = 10;

        List<ActionBoundary> allUsers =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewAction(
                                "element #" + i,
                                "smart",
                                "Py",
                                new Date(),
                                "fda@gmail.com",
                                "space",
                                new HashMap<>()))
                        .peek(action -> action.setKey("#" + ++counter))
                        .peek(action -> action.setActionSmartSpace("yo"))
                        .map(ActionBoundary::new)
                        .collect(Collectors.toList());

        List<ActionEntity> actualResult =
                Arrays.stream(Objects.requireNonNull(
                        this.restTemplate.postForObject(
                                this.baseUrl,
                                allUsers,
                                ActionBoundary[].class,
                                ADMIN_SMARTSPACE,
                                ADMIN_EMAIL)))
                        .map(ActionBoundary::convertToEntity)
                        .collect(Collectors.toList());

        List<ActionEntity> expected =
                actualResult
                        .stream()
                        .skip(5)
                        .limit(5)
                        .collect(Collectors.toList());

        // THEN the database contains the new actions

        int size = 5;
        int page = 1;
        assertThat(actionDao.readAll(size, page))
                .usingElementComparatorOnFields("key")
                .containsExactlyElementsOf(expected);
    }

    @Test
    public void testGetAllUsingPagination() {
        // GIVEN the database contains 10 messages
        int totalSize = 10;

        List<ActionBoundary> allUsers =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewAction(
                                "element #" + i,
                                "smart",
                                "Py",
                                new Date(),
                                "fda@gmail.com",
                                "space",
                                new HashMap<>()))
                        .peek(action -> action.setKey("#" + counter))
                        .map(this.actionService::store)
                        .map(ActionBoundary::new)
                        .collect(Collectors.toList());

        List<ActionBoundary> expected =
                allUsers
                        .stream()
                        .skip(5)
                        .limit(5)
                        .collect(Collectors.toList());

        // WHEN I get all users using page 1 and size 5
        int page = 1;
        int size = 5;
        ActionBoundary[] result =
                this.restTemplate
                        .getForObject(
                                this.baseUrl + "?size={size}&page={page}",
                                ActionBoundary[].class,
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
    public void testGetMessagesWithInvalidType() {
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

//    @Test
//    public void testGetMessagesWithSpamType() {
//        // GIVEN the database contains 7 PLAYER users
//        // AND the database contains 3 more MANAGER users
//        int playersSize = 7;
//        IntStream
//                .range(1, playersSize + 1)
//                .mapToObj(i -> factory.createNewUser(
//                        "user #" + i,
//                        ":)",
//                        UserRole.PLAYER,
//                        (long) 200))
//                .peek(user -> user.setKey(generateUserKey()))
//                .forEach(this.actionDao::create);
//
//        int managerSize = 3;
//        IntStream
//                .range(playersSize + 1, playersSize + 1 + managerSize)
//                .mapToObj(i -> factory.createNewUser(
//                        "user #" + i,
//                        ":)",
//                        UserRole.PLAYER,
//                        (long) 200))
//                .peek(user -> user.setKey(generateUserKey()))
//                .forEach(this.actionDao::create);
//
//
//        String role = "PLAYER";
//        int size = 10;
//        int page = 0;
//
//        UserBoundary[] results =
//                this.restTemplate
//                        .getForObject(
//                                this.baseUrl + "?size={size}&page={page}",
//                                UserBoundary[].class,
//                                ADMIN_SMARTSPACE,
//                                ADMIN_EMAIL,
//                                size,
//                                page);
//
//        // THEN I receive 7 users
//        assertThat(results)
//                .hasSize(playersSize);
//    }

}

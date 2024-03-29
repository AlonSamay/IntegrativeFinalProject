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
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.logic.ActionServiceImpl;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class ActionIntegrationTests {
    private static final String ADMIN_SMARTSPACE = "2019BTal.Cohen";
    private static final String ADMIN_EMAIL = "alon@gmail.com";

    private String baseUrl;
    private String adminUrl;
    private String actionUrl;
    private int port;

    private EntityFactory factory;
    private ActionServiceImpl actionService;
    private RestTemplate restTemplate;

    private EnhancedElementDao<ElementKey> elementDao;
    private EnhancedActionDao actionDao;
    private EnhancedUserDao<UserKey> userDao;

    private static int counter = 0;
    private ElementKey key;

    @Autowired
    public void setActionDao(EnhancedActionDao actionDao) {
        this.actionDao = actionDao;
    }

    @Autowired
    public void setElementDao(EnhancedElementDao<ElementKey> elementDao) {
        this.elementDao = elementDao;
    }

    @Autowired
    public void setUserDao(EnhancedUserDao<UserKey> userDao) {
        this.userDao = userDao;
    }

    @LocalServerPort
    public void setPort(int port) {
        this.port = port;
    }

    @Autowired
    public void setActionService(ActionServiceImpl actionService) {
        this.actionService = actionService;
    }

    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void init() {
        this.baseUrl = "http://localhost:" + port + "/smartspace";
        this.adminUrl = "/admin/actions/{adminSmartspace}/{adminEmail";
        this.actionUrl = "/actions";
        this.restTemplate = new RestTemplate();
    }

    @Before
    public void addElement() {
        this.actionDao.deleteAll();
        ElementEntity elementEntity = factory.createNewElement("a",
                "ab",
                new Location(5.4, 3.7),
                new Date(),
                "fsda@gmail.com",
                "gfsd",
                false,
                null);
        key = elementDao.create(elementEntity).getKey();

    }

    @After
    public void tearDown() {
        this.actionDao.deleteAll();
        this.elementDao.deleteAll();
    }

    @Test
    public void testActionsPostRequest() {
        // GIVEN element in database

        // WHEN 10 action boundaries are posted to the server
        int totalSize = 10;

        Map<String, Object> details = new HashMap<>();
        details.put("key1", "hello ");

        List<ActionBoundary> allActions =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewAction(
                                key.getId(),
                                key.getSmartspace(),
                                "Py",
                                new Date(),
                                "fda@gmail.com",
                                "space",
                                details))
                        .map(ActionBoundary::new)
                        .collect(Collectors.toList());

        List<ActionEntity> actualResult =
                Arrays.stream(Objects.requireNonNull(
                        this.restTemplate.postForObject(
                                this.baseUrl,
                                allActions,
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
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testGetAllUsingPagination() {
        // GIVEN database which contains 10 actions and 1 element
        int totalSize = 10;

        Map<String, Object> details = new HashMap<>();
        details.put("key1", "hello ");

        List<ActionBoundary> allUsers =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewAction(
                                key.getId(),
                                key.getSmartspace(),
                                "Py",
                                new Date(),
                                "fda@gmail.com",
                                "space",
                                details))
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
                .usingElementComparatorOnFields("actionKey")
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test(expected = Exception.class)
    public void testGetMessagesWithInvalidElement() {
        // GIVEN nothing

        // WHEN I invoke GET of users by the role of "TEMP"
        String type = "TEMP";
        this.restTemplate
                .getForObject(
                        this.baseUrl + "/{type}",
                        ActionBoundary[].class,
                        type);

        // THEN I receive an error status
    }

    @Test
    public void testActionInvoke() {
        // GIVEN element in the DB
        ElementEntity element = factory.createNewElement(
                "hello",
                "good",
                new Location(4.2, 3.2),
                new Date(),
                "samay@gmail.com",
                ADMIN_SMARTSPACE,
                false,
                null);
        element.setKey(new ElementKey("50", ADMIN_SMARTSPACE));
        elementDao.create(element);

        // WHEN I invoke post of an action on the element
        ActionEntity entity = factory.createNewAction(
                "50",
                "ADMIN_SMARTSPACE",
                "bad",
                new Date(),
                "samay@gmail.com",
                "smartspace",
                null);
        ActionBoundary boundary = new ActionBoundary(entity);
        ActionBoundary boundaryFromDB = restTemplate.postForObject(baseUrl + actionUrl, boundary, ActionBoundary.class);

        // THEN the action will be added into the DB
        assertThat(boundary).isEqualToComparingFieldByFieldRecursively(boundaryFromDB);
    }
}

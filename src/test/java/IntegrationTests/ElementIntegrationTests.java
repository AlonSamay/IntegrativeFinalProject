package IntegrationTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import smartspace.Application;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ElementBoundary;
import smartspace.logic.ElementServiceImp;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class ElementIntegrationTests {

    private static final String ADMIN_SMARTSPACE = "2019BTal.Cohen";
    private static final String ADMIN_EMAIL = "alon@gmail.com";

    private MongoTemplate mongoTemplate;

    private EntityFactory factory;
    private String adminUrl;
    private String userUrl;
    private String baseUrl;
    private int port;
    private EnhancedElementDao<ElementKey> elementDao;
    private ElementServiceImp elementService;
    private RestTemplate restTemplate;

    @Autowired
    public void setElementDao(EnhancedElementDao<ElementKey> elementDao) {
        this.elementDao = elementDao;
    }

    @Autowired
    public void setElementService(ElementServiceImp elementService) {
        this.elementService = elementService;
    }

    private static int counter = 0;

    public ElementIntegrationTests() {

    }

    @LocalServerPort
    public void setPort(int port) {
        this.port = port;
    }


    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void init() {
        this.adminUrl = "/admin/elements/{adminSmartSpace}/{ADMIN_EMAIL}";
        this.userUrl = "/elements/219BTal.Cohen/alon@gmail.com";
        this.baseUrl = "http://localhost:" + port + "/smartspace";
        this.restTemplate = new RestTemplate();
    }


    public ElementKey generateElementKey() {
        ElementKey key = new ElementKey();
        key.setElementId("Bla" + (++counter));
        key.setElementSmartSpace("mySmartSpace");
        return key;
    }

    @Before
    public void onStart() {
        this.elementDao.deleteAll();
    }


    @After
    public void tearDown() {
        this.elementDao.deleteAll();
    }

    @Test
    public void testElementsPostRequest() {
        // GIVEN nothing

        // WHEN 10 user boundaries are posted to the server
        int totalSize = 10;
        Map<String, Object> details = new HashMap<>();
        details.put("key1", "hello ");
        Location loc = new Location(10, 10);


        List<ElementBoundary> allElements =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewElement(
                                "name #" + i,
                                "myType",
                                loc,
                                new Date(),
                                "test@gmail.com",
                                "mySmartSpace",
                                false,
                                details))
                        .peek(element -> element.setKey(generateElementKey()))
                        .map(ElementBoundary::new)
                        .collect(Collectors.toList());

        List<ElementEntity> actualResult =
                Arrays.stream(Objects.requireNonNull(
                        this.restTemplate.postForObject(
                                this.baseUrl,
                                allElements,
                                ElementBoundary[].class,
                                ADMIN_SMARTSPACE,
                                ADMIN_EMAIL)))
                        .map(ElementBoundary::convertToEntity)
                        .collect(Collectors.toList());

        List<ElementEntity> expected =
                actualResult
                        .stream()
                        .skip(4)
                        .limit(5)
                        .collect(Collectors.toList());

        // THEN the database contains the new actions
        // WHEN I get all users using page 1 and size 5
        int page = 1;
        int size = 5;
        ElementBoundary[] result =
                this.restTemplate
                        .getForObject(
                                this.baseUrl + "?size={size}&page={page}",
                                ElementBoundary[].class,
                                ADMIN_SMARTSPACE,
                                ADMIN_EMAIL,
                                size,
                                page);

        // THEN the result contains 5 users of the users inserted to the database
        assertThat(elementDao.readAll(size, page))
                .usingElementComparatorOnFields("key")
                .containsExactlyInAnyOrderElementsOf(expected);
    }


    @Test
    public void testGetAllUsingPagination() {
        // GIVEN the database contains 10 messages
        int totalSize = 10;

        Map<String, Object> details = new HashMap<>();
        details.put("key1", "hello ");

        List<ElementBoundary> allElements =
                IntStream
                        .range(1, totalSize + 1)
                        .mapToObj(i -> factory.createNewElement(
                                "name #" + i,
                                "myType",
                                new Location(4.5, 3.2),
                                new Date(),
                                "test@gmail.com",
                                "mySmartSpace",
                                false,
                                details))
                        .peek(element -> element.setKey(generateElementKey()))
                        .map(this.elementService::store)
                        .map(ElementBoundary::new)
                        .collect(Collectors.toList());

        List<ElementBoundary> expected =
                allElements
                        .stream()
                        .skip(4)
                        .limit(5)
                        .collect(Collectors.toList());

        // WHEN I get all users using page 1 and size 5
        int page = 1;
        int size = 5;
        ElementBoundary[] result =
                this.restTemplate
                        .getForObject(
                                this.baseUrl + "?size={size}&page={page}",
                                ElementBoundary[].class,
                                ADMIN_SMARTSPACE,
                                ADMIN_EMAIL,
                                size,
                                page);

        // THEN the result contains 5 users of the users inserted to the database
        assertThat(result)
                .usingElementComparatorOnFields("key")
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreateElement() {
        // GIVEN nothing

        // WHEN adding new element
        ElementEntity entity = factory.createNewElement(
                "hello",
                "good",
                new Location(4.2, 3.2),
                new Date(),
                "samay@gmail.com",
                ADMIN_SMARTSPACE,
                false,
                null);
        restTemplate.postForEntity(this.baseUrl + this.userUrl, entity, ElementBoundary.class);

        // THEN the new element is added
        Optional<ElementEntity> entityFromDB = elementDao.readById(entity.getKey());

        assert (entityFromDB.isPresent());
    }
}
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.Application;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.dao.nonrdb.nonRdbUserDao;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {
        "spring.profiles.active=default"})
public class KeyGenerationTests {
    private EntityFactory factory;
    private nonRdbActionDao actionDao;
    private nonRdbElementDao elementDao;

    private nonRdbUserDao userDao;

    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    @Autowired
    public void setActionDao(nonRdbActionDao actionDao) {
        this.actionDao = actionDao;
    }

    @Autowired
    public void setElementDao(nonRdbElementDao elementDao) {
        this.elementDao = elementDao;
    }

    @Autowired
    public void setUserDao(nonRdbUserDao userDao) {
        this.userDao = userDao;
    }

    @After
    public void teardown() {
        this.actionDao.deleteAll();
        this.elementDao.deleteAll();
        this.userDao.deleteAll();
    }

    @Test
    public void testNewActionIdsAreUnique() {
        Set<ActionKey> keys = IntStream.range(1, 11)
                .mapToObj(i -> this.factory.createNewAction(
                        "elemnt " + i,
                        "mysmartspace",
                        "mytype",
                        new Date(),
                        "tom@gmail.com",
                        "newsmartspace",
                        null))
                .map(this.actionDao::create)
                .map(ActionEntity::getKey)
                .collect(Collectors.toSet());

        assertThat(keys).hasSize(10);
    }

    @Test
    public void testNewElementIdsAreUnique() {
        Set<ElementKey> keys = IntStream.range(1, 11)
                .mapToObj(i -> this.factory.createNewElement(
                        "shasho" + i,
                        "mytype",
                        new Location(5.4, 3.7),
                        new Date(),
                        "shlomi@gmail.com",
                        "shlomiShashu",
                        false,
                        new HashMap<>()))
                .map(this.elementDao::create)
                .map(ElementEntity::getKey)
                .collect(Collectors.toSet());

        assertThat(keys).hasSize(10);
    }

    @Test
    public void testNewUserIdsAreUnique() {
        Set<UserKey> keys = IntStream.range(1, 11)
                .mapToObj(i -> this.factory.createNewUser(
                        "alon@gmail.com",
                        "TalCohen2019.B",
                        "alons" + i,
                        "SmileyShelTom",
                        UserRole.PLAYER,
                        (long) 250 + i))
                .map(this.userDao::create)
                .map(UserEntity::getKey)
                .collect(Collectors.toSet());

        assertThat(keys).hasSize(10);
    }
}





package DaoUnitTests;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.Application;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactoryImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {
        "spring.profiles.active=default"})
public class ActionsDaoTests {
    private EntityFactoryImpl factory;
    private nonRdbActionDao actionDao;

    @Autowired
    public void setFactory(EntityFactoryImpl factory) {
        this.factory = factory;
    }

    @Autowired
    public void setActionDao(nonRdbActionDao actionDao) {
        this.actionDao = actionDao;
    }

    @After
    public void teardown() {
        this.actionDao.deleteAll();
    }

    @Test
    public void testReadSingleAction() {
        ActionEntity newAction = factory.createNewAction(
                "abc",
                "def",
                "fdsfsd",
                new Timestamp(new Date().getTime()),
                "csda@gmail.com",
                "fds",
                null);
        actionDao.create(newAction);
        List<ActionEntity> actual = actionDao.readAll();
        assertThat(actual.get(0)).isEqualToComparingFieldByField(newAction);
    }

    @Test
    public void testReadAll() {
        List<ActionEntity> newActions = createActions(6);
        List<ActionEntity> actual = this.actionDao.readAll();
        assertThat(actual).hasSize(6);
        assertThat(actual)
                .usingElementComparatorOnFields("actionId")
                .containsExactlyElementsOf(
                        new ArrayList<>(newActions));
    }

    @Test
    public void testReadWithDelete() {
        testReadSingleAction();
        actionDao.deleteAll();
        List<ActionEntity> actual = actionDao.readAll();
        assertThat(actual).isEmpty();
    }

    private List<ActionEntity> createActions(int size) {
        return IntStream.range(1, size + 1)
                .mapToObj(i -> this.factory.createNewAction(
                        "element #" + i,
                        "ds",
                        "vdf",
                        new Timestamp(new Date().getTime()),
                        "fds",
                        "dasfsa",
                        null))
                .map(this.actionDao::create)
                .collect(Collectors.toList());
    }
}
package DaoUnitTests;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.Application;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
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
public class ElementsDaoTests {
    private EntityFactoryImpl factory;
    private nonRdbElementDao elementDao;

    @Autowired
    public void setFactory(EntityFactoryImpl factory) {
        this.factory = factory;
    }

    @Autowired
    public void setElementDao(nonRdbElementDao elementDao) {
        this.elementDao = elementDao;
    }

    @After
    public void teardown() {
        this.elementDao.deleteAll();
    }

    @Test
    public void testReadSingleElement() {
        ElementEntity newElement = factory.createNewElement(
                "abc",
                "def",
                new Location(5, 4),
                new Timestamp(new Date().getTime()),
                "csda@gmail.com",
                "fds",
                false,
                null);
        elementDao.create(newElement);
        List<ElementEntity> actual = elementDao.readAll();
        assertThat(actual.get(0)).isEqualToComparingFieldByFieldRecursively(newElement);
    }

    @Test
    public void testReadAll() {
        List<ElementEntity> newElements = createElements(6);
        List<ElementEntity> actual = this.elementDao.readAll();
        assertThat(actual).hasSize(6);
        assertThat(actual)
                .usingElementComparatorOnFields("elementKey")
                .containsExactlyElementsOf(
                        new ArrayList<>(newElements));
    }

    @Test
    public void testReadWithDelete() {
        testReadSingleElement();
        elementDao.deleteAll();
        List<ElementEntity> actual = elementDao.readAll();
        assertThat(actual).isEmpty();
    }

    private List<ElementEntity> createElements(int size) {
        return IntStream.range(1, size + 1)
                .mapToObj(i -> this.factory.createNewElement(
                        "element #" + i,
                        "gs",
                        new Location(643, 312),
                        new Timestamp(new Date().getTime()),
                        "gf@gfd.com",
                        "fsd",
                        i % 2 == 0,
                        null))
                .map(this.elementDao::create)
                .collect(Collectors.toList());
    }
}





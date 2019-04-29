import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.Application;
import smartspace.dao.ActionDao;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest (classes = Application.class)
@TestPropertySource(properties = {
        "spring.profiles.active=default"})
public class BoundaryTests {
    private EntityFactory factory;

    private ActionDao actionDao;

    @Autowired
    public void setFactory(EntityFactory factory) {
        this.factory = factory;
    }

    //TODO :Need To fix
//    @Autowired
//    public void setActionDao(ActionDao action) {
//        this.actionDao = action;
//    }

    @Test
    public void testUserConversion() {
        UserEntity user1 = this.factory.createNewUser(
                "yanai1@gmail.com",
                "Yanai",
                "Yanai100",
                UserRole.PLAYER,
                (long) 123);

        UserBoundary boundary = new UserBoundary(user1);

        assertThat(boundary.convertToEntity()).isEqualTo(user1);
    }

    @Test
    public void testActionConversion() {
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

        ActionBoundary boundary = new ActionBoundary(newAction);

        assertThat(actual.get(0)).isEqualTo(boundary.convertToEntity());
    }

    @Test
    public void testElementConversion() {
        ElementEntity newElement = factory.createNewElement(
                "abc",
                "def",
                new Location(5, 4),
                new Timestamp(new Date().getTime()),
                "csda@gmail.com",
                "fds",
                false,
                null);

        ElementBoundary boundary = new ElementBoundary(newElement);

        assertThat(boundary.convertToEntity()).isEqualTo(newElement);
    }

}
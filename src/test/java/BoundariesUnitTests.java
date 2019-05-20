import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.Application;
import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.layout.UserBoundary;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest (classes = Application.class)
@TestPropertySource(properties = {
        "spring.profiles.active=default"})
public class BoundariesUnitTests {
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
        UserEntity user1 = factory.createNewUser(
                "AlonSamay@gmail.com",
                "TalCohen2019.B",
                "Babi",
                "C",
                UserRole.PLAYER,
                (long) 123
        );

        UserBoundary boundary = new UserBoundary(user1);

        assertThat(boundary.convertToEntity()).isEqualToComparingFieldByFieldRecursively(user1);
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

        ActionBoundary boundary = new ActionBoundary(newAction);

        assertThat(newAction).isEqualToComparingFieldByFieldRecursively(boundary.convertToEntity());
    }

    @Test
    public void testElementConversion() {
//        ElementEntity newElement = factory.createNewElement(
//                "abc",
//                "def",
//                new Location(5, 4),
//                new Timestamp(new Date().getTime()),
//                "csda@gmail.com",
//                "gsdfn",
//                false,
//                null);
//        newElement.setKey(new ElementKey("1"));
//
//        ElementBoundary boundary = new ElementBoundary(newElement);
//
//        assertThat(boundary.convertToEntity()).isEqualToComparingFieldByFieldRecursively(newElement);
    }
}
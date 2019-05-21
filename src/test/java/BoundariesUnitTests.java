import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import smartspace.Application;
import smartspace.dao.ActionDao;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
        UserEntity user1 = this.factory.createNewUser(
                "Babi",
                "B",
                UserRole.PLAYER,
                (long) 123);
        user1.setKey(new UserKey("alon@gmail.com"));

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
        ElementEntity newElement = factory.createNewElement(
                "abc",
                "def",
                new Location(5, 4),
                new Timestamp(new Date().getTime()),
                "csda@gmail.com",
                "gsdfn",
                false,
                null);
//        newElement.setKey(new ElementKey("1"));

        ElementBoundary boundary = new ElementBoundary(newElement);

        assertThat(boundary.convertToEntity()).isEqualToComparingFieldByFieldRecursively(newElement);
    }
}
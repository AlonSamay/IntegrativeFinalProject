package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.dao.nonrdb.nonRdbUserDao;
import smartspace.data.*;
import smartspace.logic.UserServiceImpl;

import java.util.Map;

@PropertySource("cart.properties")
@Component
public class PayPlugin extends CartPlugin {

    private Environment env;
    private UserServiceImpl userService;

    @Autowired
    public PayPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao, Environment env, UserServiceImpl userService) {
        super(actionDao, jackson, elementDao);
        this.env = env;
        this.userService = userService;
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        try {
            CartInput input = this.jackson
                    .readValue(this.jackson.writeValueAsString(actionEntity.getMoreAttributes()),
                            CartInput.class);

            ElementEntity cartToUpdate = this.getCartByKey(
                    actionEntity.getElementId(), actionEntity.getElementSmartSpace());

            cartToUpdate.setExpired(true);

            Map<String, Object> moreAtt = cartToUpdate.getMoreAttributes();
            moreAtt.put(env.getProperty("fields.cart.address"), input.getAddress());
            moreAtt.put(env.getProperty("fields.cart.creditcardnumber"), input.getCreditCardNumber());
            moreAtt.put(env.getProperty("fields.cart.date"), input.getExpiryDate());
            moreAtt.put(env.getProperty("fields.cart.cvv"), input.getCvv());
            moreAtt.put(env.getProperty("fields.cart.creditcardownerid"), input.getCreditCardOwnerId());

            cartToUpdate.setMoreAttributes(moreAtt);
            elementDao.create(cartToUpdate);
            long newPointsForUser = this.updateUserPoints(actionEntity.getPlayerEmail(), actionEntity.getPlayerSmartSpace(),
                    (((Double) (cartToUpdate.getMoreAttributes().get(env.getProperty("fields.cart.amount"))))).floatValue());
            moreAtt.clear();
            moreAtt.put("points", newPointsForUser);
            actionEntity.setMoreAttributes(moreAtt);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return actionEntity;
    }

    private long updateUserPoints(String playerEmail, String playerSmartSpace, float amount) {
        UserEntity buyer = this.userService.get(playerSmartSpace, playerEmail);
        long currentPoints = buyer.getPoints();
        long newPoints = (long) (currentPoints + amount / 3);
        buyer.setPoints(newPoints);
        userService.store(buyer);
        return newPoints;
    }
}

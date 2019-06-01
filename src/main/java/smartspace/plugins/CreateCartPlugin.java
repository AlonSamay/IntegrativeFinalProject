package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@PropertySource("application.properties")
@Component
public class CreateCartPlugin extends ActionPlugIn {


    @Autowired
    public CreateCartPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson, elementDao);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        // get cartGenerator from db to assign new cart's creator email and smartspace as the manager from cartGenerator
        ElementKey key = new ElementKey(actionEntity.getElementId(), actionEntity.getElementSmartSpace());
        ElementEntity cartGenerator = getElementByKey(key);

        ElementEntity cart = new ElementEntity();

        cart.setType("cart");
        cart.setCreationTimeStamp(new Date());
        cart.setExpired(false);
        cart.setLocation(new Location(0,0));
        cart.setCreatorEmail(cartGenerator.getCreatorEmail());
        cart.setCreatorSmartSpace(cartGenerator.getCreatorSmartSpace());

        UserKey ownerKey = new UserKey(actionEntity.getPlayerEmail(), actionEntity.getPlayerSmartSpace());
        CartInput cartProperties  = new CartInput(ownerKey);
        cart.setMoreAttributes(this.jackson.convertValue(cartProperties,Map.class));

        ElementEntity cartFromDB = elementDao.create(cart);
        cartFromDB.setName("cart: " + cartFromDB.getElementId());
        elementDao.update(cartFromDB);

        // update actionEntity moreAttributes with the cartID
        HashMap<String,Object> moreAttributes=new HashMap<>();
        moreAttributes.put("cartKey", cartFromDB.getElementKey());
        actionEntity.setMoreAttributes(moreAttributes);

        return actionEntity;
    }

}

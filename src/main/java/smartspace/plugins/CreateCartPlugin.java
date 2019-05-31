package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import smartspace.dao.ActionDao;
import smartspace.dao.ElementDao;
import smartspace.dao.EnhancedActionDao;
import smartspace.data.*;
import smartspace.layout.exceptions.NotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@PropertySource("application.properties")
@Component
public class CreateCartPlugin extends ActionPlugIn {

    private ElementDao elementDao;

    @Autowired
    public CreateCartPlugin(ActionDao actionDao, ObjectMapper jackson) {
        super(actionDao, jackson);
    }

    @Autowired
    public void setElementDao(ElementDao elementDao) {
        this.elementDao = elementDao;
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

        Map<String, Object> moreAttributes = new HashMap<>();
        UserKey ownerKey = new UserKey(actionEntity.getPlayerEmail(), actionEntity.getPlayerSmartSpace());
        moreAttributes.put("playerKey", ownerKey);
        cart.setMoreAttributes(moreAttributes);

        ElementEntity cartFromDB = elementDao.create(cart);
        cart.setName("cart: " + cartFromDB.getElementId());
        elementDao.update(cartFromDB);

        // update actionEntity moreAttributes with the cartID
        moreAttributes.clear();
        moreAttributes.put("cartKey", cartFromDB.getElementKey());
        actionEntity.setMoreAttributes(moreAttributes);

        return actionEntity;
    }

    private ElementEntity getElementByKey(ElementKey key) {
        Optional<ElementEntity> optEntity = elementDao.readById(key);

        if(!optEntity.isPresent()) {
            throw new NotFoundException(this.getClass().getSimpleName() + ": Can't find element with key: " + key);
        }
        return optEntity.get();
    }
}

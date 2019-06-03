package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Component
public class CheckOutPlugin extends ActionPlugIn{

    @Autowired
    public CheckOutPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson,elementDao);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        ElementKey key = new ElementKey(actionEntity.getElementId(), actionEntity.getElementSmartSpace());
        ElementEntity userCart = getElementByKey(key);

        userCart.setExpired(true);
        elementDao.update(userCart);

        return actionEntity;
    }
}

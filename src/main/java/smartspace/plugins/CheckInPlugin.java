package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.ActionDao;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;

import java.util.Map;

@Component
public class CheckInPlugin extends ActionPlugIn {

    @Autowired
    public CheckInPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson,elementDao);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {

        ElementKey key = new ElementKey(actionEntity.getElementId(), actionEntity.getElementSmartSpace());
        ElementEntity userCart = getElementByKey(key);

        // Option 1 - get the location as Object.
        Location userLocation = (Location) actionEntity.getMoreAttributes().get("location");
        userCart.setLocation(userLocation);

        // Option 2 - get the location as Map.
        Map<String,Object> userLocationMap = (Map<String, Object>) actionEntity.getMoreAttributes().get("location");
        Location userLocation1 = new Location((long)userLocationMap.get("lat"), (long)userLocationMap.get("long"));
        userCart.setLocation(userLocation1);

        elementDao.update(userCart);

        return actionEntity;
    }
}

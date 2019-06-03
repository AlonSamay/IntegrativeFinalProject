package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LikePlugin extends ActionPlugIn{

    @Autowired
    public LikePlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson,elementDao);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        ElementKey key = new ElementKey(actionEntity.getElementId(), actionEntity.getElementSmartSpace());
        ElementEntity product = getElementByKey(key);

        Map<String,Object> productMap = product.getMoreAttributes();
        AtomicInteger likeCount = (AtomicInteger) productMap.get("like");
        likeCount.incrementAndGet();
        productMap.replace("like", likeCount);
        product.setMoreAttributes(productMap);

        elementDao.update(product);

        return actionEntity;
    }
}

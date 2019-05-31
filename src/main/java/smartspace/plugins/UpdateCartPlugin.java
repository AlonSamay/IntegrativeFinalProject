package smartspace.plugins;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.Map;

@Component
public class UpdateCartPlugin extends ActionPlugIn {


    @Autowired
    public UpdateCartPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson, elementDao);
        jackson.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        try {
            CartInput input = this.jackson
                    .readValue(this.jackson.writeValueAsString(actionEntity.getMoreAttributes()),
                            CartInput.class);


            ElementEntity cartToUpdate = this.getElementByKey(
                    new ElementKey(actionEntity.getElementId(), actionEntity.getElementSmartSpace()));

            cartToUpdate.setMoreAttributes(this.jackson.convertValue(input, Map.class));
            elementDao.update(cartToUpdate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return actionEntity;
    }
}



package smartspace.plugins;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.Map;

public abstract class CartPlugin extends ActionPlugIn {

    public CartPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson, elementDao);
        jackson.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }


    public ElementEntity getCartByKey(String elemnetId, String elementSmartSpace) {
        return this.getElementByKey(
                new ElementKey(elemnetId, elementSmartSpace));

    }
}
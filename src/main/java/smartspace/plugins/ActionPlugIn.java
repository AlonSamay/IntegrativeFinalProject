package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.ActionDao;
import smartspace.dao.ElementDao;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.layout.exceptions.NotFoundException;

import java.util.Optional;

public abstract class ActionPlugIn implements PluginCommand {

    nonRdbActionDao actionDao;
    nonRdbElementDao elementDao;
    ObjectMapper jackson;
    public ActionPlugIn(){

    }

    public ActionPlugIn(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        this.actionDao = actionDao;
        this.jackson = jackson;
        this.elementDao = elementDao;
    }

    public ElementEntity getElementByKey(ElementKey key) {
        Optional<ElementEntity> optEntity = elementDao.readById(key);

        if (!optEntity.isPresent()) {
            throw new NotFoundException(this.getClass().getSimpleName() + ": Can't find element with key: " + key);
        }
        return optEntity.get();
    }

}

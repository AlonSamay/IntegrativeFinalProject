package smartspace.logic;

import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.layout.FieldException;

import java.util.List;
import java.util.Optional;

@PropertySource("application.properties")
@Service
public class ActionService extends Validator implements ServicePattern<ActionEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private EnhancedActionDao actionDao;
    private EnhancedElementDao elementDao;

    @Autowired
    public ActionService(EnhancedActionDao actionDao,EnhancedElementDao elementDao) {
        this.actionDao = actionDao;
        this.elementDao = elementDao;
    }

    @Override
    public List<ActionEntity> getAll(int size, int page) {
        return this.actionDao.readAll(size, page);
    }

    @Override
    @Transactional
    public ActionEntity store(ActionEntity actionEntity) {
        if (validate(actionEntity)) {
            actionEntity.setCreationTimeStamp(new Date());
            return this.actionDao.create(actionEntity);
        } else
            throw new FieldException(this.getClass().getSimpleName());
    }

    private boolean validate(ActionEntity actionEntity) {

        return this.isValid(actionEntity.getActionId()) &&
                !actionEntity.getActionSmartSpace().equals(this.smartSpaceName) &&
                this.isValid(actionEntity.getElementSmartSpace()) &&
                this.isValid(actionEntity.getElementId()) &&
                this.isElementExist(actionEntity.getElementId(),actionEntity.getElementSmartSpace()) &&
                this.isValid(actionEntity.getPlayerSmartSpace()) &&
                this.isValid(actionEntity.getPlayerEmail()) &&
                this.isValid(actionEntity.getActionType()) &&
                this.isValid(actionEntity.getMoreAttributes());
    }
    private boolean isElementExist(String elementId, String elementSmartSpace){
        // actions supposed to be on elements, so we should check that this element exist in our db
        ElementKey keyToCheck = new ElementKey();
        keyToCheck.setElementId(elementId);
        keyToCheck.setElementSmartSpace(elementSmartSpace);
        Optional<ElementEntity> elementFromDB = this.elementDao.readById(keyToCheck);
        return elementFromDB.isPresent();
    }
}

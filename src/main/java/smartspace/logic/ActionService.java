package smartspace.logic;

import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.layout.FieldException;

import java.util.List;

@Service
public class ActionService extends Validator implements ServicePattern<ActionEntity> {

    private EnhancedActionDao actionDao;

    @Autowired
    public ActionService(EnhancedActionDao actionDao) {
        this.actionDao = actionDao;
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
            throw new FieldException("Action Service");
    }

    private boolean validate(ActionEntity actionEntity) {

        return this.isValid(actionEntity.getActionId()) &&
                !actionEntity.getActionSmartSpace().equals("2019BTal.Cohen") &&
                this.isValid(actionEntity.getElementSmartSpace()) &&
                this.isValid(actionEntity.getElementId()) &&
                this.isValid(actionEntity.getPlayerSmartSpace()) &&
                this.isValid(actionEntity.getPlayerEmail()) &&
                this.isValid(actionEntity.getActionType()) &&
                this.isValid(actionEntity.getMoreAttributes());
    }
}

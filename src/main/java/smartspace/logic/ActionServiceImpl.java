package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.EmailAddress;
import smartspace.layout.exceptions.FieldException;
import smartspace.layout.exceptions.IlegalActionType;
import smartspace.plugins.PluginCommand;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PropertySource("application.properties")
@Service
public class ActionServiceImpl extends Validator implements ActionService<ActionEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private EnhancedActionDao actionDao;
    private EnhancedElementDao elementDao;
    private ApplicationContext ctx;

    @Autowired
    public ActionServiceImpl(EnhancedActionDao actionDao, EnhancedElementDao elementDao, ApplicationContext ctx) {
        this.actionDao = actionDao;
        this.elementDao = elementDao;
        this.ctx = ctx;
    }

//    @Autowired
//    public ActionServiceImpl(EnhancedActionDao actionDao, EnhancedElementDao elementDao) {
//        this.actionDao = actionDao;
//        this.elementDao = elementDao;
//    }

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

    @Transactional
    public ActionEntity[] storeAll(ActionEntity[] actionEntities) {
        boolean isAllValid = Arrays.stream(actionEntities).allMatch(this::validate);
        if (isAllValid) {
            return Arrays.stream(actionEntities).map(this::store).toArray(ActionEntity[]::new);
        } else
            throw new RuntimeException(this.getClass().getSimpleName());
    }

    private boolean validate(ActionEntity actionEntity) {

        return this.isValid(actionEntity.getActionId()) &&
                !actionEntity.getActionSmartSpace().equals(this.smartSpaceName) &&
                this.isValid(actionEntity.getElementSmartSpace()) &&
                this.isValid(actionEntity.getElementId()) &&
                this.isElementExist(actionEntity.getElementId(), actionEntity.getElementSmartSpace()) &&
                this.isValid(actionEntity.getPlayerSmartSpace()) &&
                this.isValid(new EmailAddress(actionEntity.getPlayerEmail())) &&
                this.isValid(actionEntity.getActionType()) &&
                this.isValid(actionEntity.getMoreAttributes());
    }

    private boolean isElementExist(String elementId, String elementSmartSpace) {
        // actions supposed to be on elements, so we should check that this element exist in our db
        ElementKey keyToCheck = new ElementKey();
        keyToCheck.setElementId(elementId);
        keyToCheck.setElementSmartSpace(elementSmartSpace);
        Optional<ElementEntity> elementFromDB = this.elementDao.readById(keyToCheck);
        return elementFromDB.isPresent();
    }

    private String createClassNameForPlugIn(String actionType){
        return "smartspace.plugins."
                + actionType.toUpperCase().charAt(0)
                + actionType.substring(1)
                + "Plugin";
    }

    @Override
    @Transactional
    public ActionEntity invoke(ActionEntity actionEntity){
        try {

            String actionType = actionEntity.getActionType();
            if(actionType != null && !actionType.trim().isEmpty()){
                String className = this.createClassNameForPlugIn(actionType);
                Class<?> theClass = Class.forName(className);
                Object plugin = ctx.getBean(theClass);
                actionEntity = ((PluginCommand) plugin).invoke(actionEntity);
                return this.store(actionEntity); //will do validation and create with dao
            }
            else {
                throw new IlegalActionType();
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

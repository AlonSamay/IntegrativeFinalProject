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
import smartspace.layout.exceptions.NotFoundException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
        validate(actionEntity);
        actionEntity.setCreationTimeStamp(new Date());
        return this.actionDao.create(actionEntity);
    }

    @Transactional
    public ActionEntity[] storeAll(ActionEntity[] actionEntities) {
        return Arrays.stream(actionEntities)
                .filter(this::validateDifferentUserSmartspace)
                .map(this::store).toArray(ActionEntity[]::new);
    }

    private boolean validateDifferentUserSmartspace(ActionEntity actionEntity) {
        if (!this.isValid(actionEntity.getKey()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's key");
        if (!this.isValid(actionEntity.getActionSmartSpace()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's smartspace");
        return !actionEntity.getActionSmartSpace().equals(smartSpaceName);

    }

    private void validate(ActionEntity actionEntity) {
        if (!this.isValid(actionEntity.getElementSmartSpace()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's element smartspace");

        if (!this.isValid(actionEntity.getElementId()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's element ID");

        if (!this.isElementExist(actionEntity.getElementId(), actionEntity.getElementSmartSpace()))
            throw new NotFoundException(this.getClass().getSimpleName() + ": Action's element isn't exist in DB");

        if (!this.isValid(actionEntity.getPlayerSmartSpace()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's player smartspace");

        if (!this.isValid(new EmailAddress(actionEntity.getPlayerEmail())))
            throw new FieldException(this.getClass().getSimpleName(), "Action's player email");

        if (!this.isValid(actionEntity.getActionType()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's type");

        if (!this.isValid(actionEntity.getMoreAttributes()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's properties");

//        return this.isValid(actionEntity.getSmartspace()) &&
//                this.isValid(actionEntity.getId()) &&
//                this.isElementExist(actionEntity.getId(), actionEntity.getSmartspace()) &&
//                this.isValid(actionEntity.getPlayerSmartSpace()) &&
//                this.isValid(new EmailAddress(actionEntity.getPlayerEmail())) &&
//                this.isValid(actionEntity.getActionType()) &&
//                this.isValid(actionEntity.getMoreAttributes());
    }

    private boolean isElementExist(String elementId, String elementSmartSpace) {
        // actions supposed to be on elements, so we should check that this element exist in our db
        ElementKey keyToCheck = new ElementKey(elementId, elementSmartSpace);
        return this.elementDao.readById(keyToCheck).isPresent();
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

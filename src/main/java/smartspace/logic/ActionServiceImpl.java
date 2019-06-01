package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.nonRdbElementDao;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementKey;
import smartspace.data.EmailAddress;
import smartspace.layout.exceptions.FieldException;
import smartspace.layout.exceptions.IlegalActionType;
import smartspace.layout.exceptions.NotFoundException;
import smartspace.plugins.PluginCommand;

import java.util.Arrays;
import java.util.List;

@PropertySource("application.properties")
@Service
public class ActionServiceImpl extends Validator implements ActionService<ActionEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private nonRdbActionDao actionDao;
    private nonRdbElementDao elementDao;
    private ApplicationContext ctx;

    @Autowired
    public ActionServiceImpl(nonRdbActionDao actionDao, nonRdbElementDao elementDao, ApplicationContext ctx) {
        this.actionDao = actionDao;
        this.elementDao = elementDao;
        this.ctx = ctx;
    }


    @Override
    public List<ActionEntity> getAll(int size, int page) {
        return this.actionDao.readAll(size, page);
    }

    @Override
    @Transactional
    public ActionEntity store(ActionEntity actionEntity) {
        validate(actionEntity);
        return this.actionDao.create(actionEntity);
    }

    @Transactional
    public ActionEntity[] storeAll(ActionEntity[] actionEntities) {
        boolean isAllValid = Arrays.stream(actionEntities).allMatch(this::validateImportedAction);
        if (isAllValid)
            return Arrays.stream(actionEntities)
                    .map(this.actionDao::create).toArray(ActionEntity[]::new);
        throw new RuntimeException("Not valid Actions");
    }

    private boolean validateImportedAction(ActionEntity actionEntity) {
        if (!this.isValid(actionEntity.getActionId()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's key");
        if (!this.isValid(actionEntity.getActionSmartSpace()))
            throw new FieldException(this.getClass().getSimpleName(), "Action's smartspace");
        if (!this.isElementExist(actionEntity.getElementId(), actionEntity.getElementSmartSpace()))
            throw new NotFoundException(this.getClass().getSimpleName() + ": Action's element isn't exist in DB");
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

    }

    private boolean isElementExist(String elementId, String elementSmartSpace) {
        // actions supposed to be on elements, so we should check that this element exist in our db
        ElementKey keyToCheck = new ElementKey(elementId, elementSmartSpace);
        return this.elementDao.readById(keyToCheck).isPresent();
    }

    private String createClassNameForPlugIn(String actionType) {
        return "smartspace.plugins."
                + actionType.toUpperCase().charAt(0)
                + actionType.substring(1)
                + "Plugin";
    }

    @Override
    @Transactional
    public ActionEntity invoke(ActionEntity actionEntity) {
        try {

            String actionType = actionEntity.getActionType();
            if (actionType != null && !actionType.trim().isEmpty()) {
                String className = this.createClassNameForPlugIn(actionType);
                try {
                    Class<?> theClass = Class.forName(className);
                    Object plugin = ctx.getBean(theClass);
                    actionEntity = ((PluginCommand) plugin).invoke(actionEntity);
                } catch (ClassNotFoundException exc) {
                    System.out.println("No plugin for this action , Saving to db ");
                }
                return this.store(actionEntity); //will do validation and create with dao
            } else {
                throw new IlegalActionType();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

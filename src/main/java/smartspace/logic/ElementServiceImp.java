package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.EmailAddress;
import smartspace.layout.exceptions.FieldException;
import smartspace.layout.exceptions.NotFoundException;

import java.util.*;

@PropertySource("application.properties")
@Service
public class ElementServiceImp extends Validator implements ElementService<ElementEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private EnhancedElementDao<ElementKey> elementDao;
    private final String LOCATION = "location";
    private final String NAME = "name";
    private final String TYPE = "type";
    private final String ALL = "all";

    @Autowired
    public ElementServiceImp(EnhancedElementDao<ElementKey> elementDao) {
        this.elementDao = elementDao;
    }

    @Override
    public List<ElementEntity> getAll(int size, int page) {
        return this.elementDao.readAll(size, page);
    }

    @Override
    @Transactional
    public ElementEntity store(ElementEntity elementEntity) {
        if (validate(elementEntity)) {
            elementEntity.setCreationTimeStamp(new Date());
            return this.elementDao.create(elementEntity);
        } else
            throw new FieldException(this.getClass().getSimpleName());
    }

    @Transactional
    public ElementEntity[] storeAll(ElementEntity[] elementEntities) {
        boolean isAllValid= Arrays.stream(elementEntities).allMatch(this::validate);
        if (isAllValid){
            return Arrays.stream(elementEntities).map(this::store).toArray(ElementEntity[]::new);
        }
        else
            throw new RuntimeException(this.getClass().getSimpleName());
    }

    @Override
    @Transactional
    public void update(ElementEntity update) {
         this.elementDao.update(update);
    }

    @Override
    @Transactional
    public ElementEntity readById(String elementSmartSpace,String elementId) {
        //CREATING A KEY FROM THE PARAMETERS GOT FROM THE URL
        ElementKey elementKey = createElementKeyFromUrl(elementSmartSpace, elementId);
        Optional<ElementEntity> elementFromDao = this.elementDao.readById(elementKey);
        if(elementFromDao.isPresent()){
            return elementFromDao.get();
        }
        else{
            throw new NotFoundException("element not found");
        }

    }

    private ElementKey createElementKeyFromUrl(String elementSmartSpace, String elementId) {
        ElementKey elementKey = new ElementKey();
        elementKey.setElementSmartSpace(elementSmartSpace);
        elementKey.setElementId(elementId);
        return elementKey;
    }

    @Override
    @Transactional
    public List<ElementEntity> getElementsBySearchTerm(String param, String value, double x, double y, double distance, int size, int page) {
        List<ElementEntity> rv = new ArrayList<>();
        switch (param) {

            case ALL:
                rv = elementDao.readAll(size,page);
                break;

            case LOCATION:
                rv = elementDao.readAllWithinLocation(new Circle(x, y, distance));
                System.err.println(rv);

                break;

            case NAME:
                rv = elementDao.readAllByName(value,size,page);
                break;

            case TYPE:
                rv = elementDao.readAllByType(value,size,page);
                break;

            default:
                rv=null;
                //TODO NEED TO THINK WHAT TO IN IN CASE OF DEFAULT
        }

        return rv;
    }

    private boolean validate(ElementEntity elementEntity) {

        return this.isValid(elementEntity.getName()) &&
                !elementEntity.getCreatorSmartSpace().equals(this.smartSpaceName) &&
                this.isValid(elementEntity.getType()) &&
                this.isValid(elementEntity.getCreatorSmartSpace()) &&
                this.isValid(new EmailAddress(elementEntity.getCreatorEmail())) &&
                this.isValid(elementEntity.getLocation().getX()) &&
                this.isValid(elementEntity.getLocation().getY()) &&
                !elementEntity.getExpired() &&
                this.isValid(elementEntity.getMoreAttributes());
        // DELETED THE VALIDATION OF NULL KEY
    }
}

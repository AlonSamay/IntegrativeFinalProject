package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.EmailAddress;
import smartspace.layout.exceptions.FieldException;
import smartspace.layout.exceptions.NotFoundException;

import java.util.*;

enum SearchTermEnum {LOCATION, NAME, TYPE, ALL}

@PropertySource("application.properties")
@Service
public class ElementServiceImp extends Validator implements ElementService<ElementEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private nonRdbElementDao elementDao;

    @Autowired
    public ElementServiceImp(nonRdbElementDao elementDao) {
        this.elementDao = elementDao;
    }

    @Override
    public List<ElementEntity> getAll(int size, int page) {
        return this.elementDao.readAll(size, page);
    }

    @Override
    @Transactional
    public ElementEntity store(ElementEntity elementEntity) {
        validate(elementEntity);
        return this.elementDao.create(elementEntity);
    }

    @Transactional
    public ElementEntity[] storeAll(ElementEntity[] elementEntities) {
        boolean isAllValid = Arrays.stream(elementEntities).allMatch(this::validateImportedElement);
        if (isAllValid) {
            return Arrays.stream(elementEntities).map(this.elementDao::create).toArray(ElementEntity[]::new);
        } else
            throw new RuntimeException("not all elements are valid");
    }

    @Override
    @Transactional
    public void update(ElementEntity update) {
        this.elementDao.update(update);
    }

    @Override
    @Transactional
    public ElementEntity readById(String elementSmartSpace, String elementId) {
        //CREATING A KEY FROM THE PARAMETERS GOT FROM THE URL
        ElementKey elementKey = new ElementKey(elementId, elementSmartSpace);
        Optional<ElementEntity> elementFromDao = this.elementDao.readById(elementKey);
        if (elementFromDao.isPresent()) {
            return elementFromDao.get();
        } else {
            throw new NotFoundException(String.format("%s not found", elementId));
        }

    }

    @Override
    @Transactional
    public List<ElementEntity> getElementsBySearchTerm(String param, String value, double x, double y, double distance, int size, int page) {
        List<ElementEntity> rv = null;
        SearchTermEnum term;
        try {
            term = SearchTermEnum.valueOf(param.toUpperCase());
        } catch (Exception ex) {
            throw new FieldException("Cannot search by " + param + ", exception details: " + ex);
        }
        switch (term) {
            case ALL:
                rv = elementDao.readAll(size, page);
                break;
            case LOCATION:
                rv = elementDao.readAllWithinLocation(new Circle(x, y, distance));
                break;
            case NAME:
                rv = elementDao.readAllByName(value, size, page);
                break;
            case TYPE:
                rv = elementDao.readAllByType(value, size, page);
                break;
        }

        return rv;
    }

    private boolean validateImportedElement(ElementEntity elementEntity) {
        return elementEntity.getKey() != null
                && this.isValid(elementEntity.getElementKey().getId())
                && this.isValid(elementEntity.getElementKey().getSmartspace())
                && !elementEntity.getKey().getSmartspace().equals(smartSpaceName);
    }

    private void validate(ElementEntity elementEntity) {
        if (!this.isValid(new EmailAddress(elementEntity.getCreatorEmail())))
            throw new FieldException(this.getClass().getSimpleName(), "Element's creator email");

        if (!this.isValid(elementEntity.getCreatorSmartSpace()))
            throw new FieldException(this.getClass().getSimpleName(), "Element's creator smartspace");

        if (!this.isValid(elementEntity.getName()))
            throw new FieldException(this.getClass().getSimpleName(), "Element's name");

        if (!this.isValid(elementEntity.getType()))
            throw new FieldException(this.getClass().getSimpleName(), "Element's type");

        if (elementEntity.isExpired())
            throw new FieldException(this.getClass().getSimpleName() + ": Element expired");

    }

}

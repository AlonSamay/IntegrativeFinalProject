package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.layout.FieldException;

import java.util.Date;
import java.util.List;

@PropertySource("application.properties")
@Service
public class ElementService extends Validator implements ServicePattern<ElementEntity> {

    @Value("${SmartSpace.name.property}")
    private String smartSpaceName;

    private EnhancedElementDao<ElementKey> elementDao;

    @Autowired
    public ElementService(EnhancedElementDao<ElementKey> elementDao) {
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

    private boolean validate(ElementEntity elementEntity) {

        return this.isValid(elementEntity.getName()) &&
                !elementEntity.getCreatorSmartSpace().equals(this.smartSpaceName) &&
                this.isValid(elementEntity.getType()) &&
                this.isValid(elementEntity.getCreatorSmartSpace()) &&
                this.isValid(elementEntity.getCreatorEmail()) &&
                this.isValid(elementEntity.getLocation().getX()) &&
                this.isValid(elementEntity.getLocation().getY()) &&
                !elementEntity.getExpired() &&
                this.isValid(elementEntity.getMoreAttributes()) &&
                this.isValid(elementEntity.getElementKey().getElementId());
    }
}

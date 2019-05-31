package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.layout.exceptions.NotFoundException;

import java.util.Map;
import java.util.Optional;

public class updateCartPlugin extends ActionPlugIn {

    @Autowired
    private nonRdbElementDao elementDao;

    @Autowired
    public updateCartPlugin(EnhancedActionDao actionDao, ObjectMapper jackson) {
        super(actionDao, jackson);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        try {
            UpdateCartInput input = this.jackson
                    .readValue(this.jackson.writeValueAsString(actionEntity.getMoreAttributes()),
                            UpdateCartInput.class);
            System.out.println(input.getAdresss());
            System.out.println(input.getAmoount());
            Optional<ElementEntity> opCartToUpdate = elementDao.readById(
                    new ElementKey(actionEntity.getElementId(), actionEntity.getElementSmartSpace()));
            if (!opCartToUpdate.isPresent())
                throw new NotFoundException("no such cart with this details ");
            ElementEntity cartToUpdate = opCartToUpdate.get();

            cartToUpdate.setMoreAttributes(this.jackson.convertValue(input, Map.class));
            elementDao.create(cartToUpdate);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return actionEntity;
    }
}



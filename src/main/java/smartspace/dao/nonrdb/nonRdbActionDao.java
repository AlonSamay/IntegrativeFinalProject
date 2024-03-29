package smartspace.dao.nonrdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.IdGenerator;
import smartspace.dao.IdGeneratorCrud;
import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Repository
public class nonRdbActionDao implements EnhancedActionDao {

    private NRdbActionCrud actionCrud;
    private final String ACTION_ID = "actionId";
    private IdGeneratorCrud idGeneratorCrud;


    @Autowired
    public nonRdbActionDao(NRdbActionCrud actionCrud, IdGeneratorCrud idGeneratorCrud) {
        this.actionCrud = actionCrud;
        this.idGeneratorCrud = idGeneratorCrud;
    }

    @Override
    @Transactional
    public ActionEntity create(ActionEntity action) {
        if (action.getKey() == null) {
            //new action created- new key for that action
            ActionKey newKey=new ActionKey(this.idGeneratorCrud.save(new IdGenerator()).getNextId());
            action.setKey(newKey);
            action.setCreationTimeStamp(new Date());
        }
        return this.actionCrud.save(action);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ActionEntity> readAll() {
        // SELECT ID, text FROM MESSAGES ORDERY BY ID
        List<ActionEntity> rv = new ArrayList<>();
        this.actionCrud
                .findAll()
                .forEach(element -> rv.add(element));
        return rv;
    }


    @Override
    @Transactional
    public void deleteAll() {
        this.actionCrud.deleteAll();
    }

    @Override
    public List<ActionEntity> readAll(int size, int page) {
        return actionCrud.findAll(PageRequest.of(page, size, ASC, ACTION_ID)).getContent();
    }
}

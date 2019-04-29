package smartspace.dao.nonrdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.*;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class nonRdbElementDao implements EnhancedElementDao<ElementKey> {
    private NRdbElementCrud elementCrud;
    private IdGeneratorCrud idGeneratorCrud;



    @Autowired
    public nonRdbElementDao(NRdbElementCrud elementCrud, IdGeneratorCrud idGeneratorCrud) {
        this.idGeneratorCrud = idGeneratorCrud;
        this.elementCrud = elementCrud;

    }

    @Override
    @Transactional
    public ElementEntity create(ElementEntity element) {
        if(element.getElementKey() == null) {
            //SMART SPACE DOESN'T EXIST - CREATE NEW KEY
            element.setElementKey(new ElementKey(this.idGeneratorCrud.save(new IdGenerator()).getNextId()));
        }
        else{
            //SMARTSPACE ALREADY EXIST - ASSIGNING ID
            element.getElementKey().setElementId(this.idGeneratorCrud.save(new IdGenerator()).getNextId());
        }
        return this.elementCrud.save(element);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ElementEntity> readAll() {
        List<ElementEntity> rv = new ArrayList<>();
        this.elementCrud
                .findAll()
                .forEach(element->rv.add(element));
        return rv;
    }

    @Override
    public Optional<ElementEntity> readById(ElementKey key) {
        System.err.println(key);
        return this.elementCrud.findById(key);
    }


//
//	@Override
//	@Transactional(readOnly = true)
//	public Optional<ElementEntity> readById(K key) {
//		return this.elementCrud.findById(key);
//	}

    @Override
    @Transactional
    public void update(ElementEntity update) {
        if(this.elementCrud.existsById(update.getKey())) {
            this.elementCrud.save(update);
        }else {
            throw new RuntimeException("no element with id: " + update.getKey());
        }


    }

    @Override
    @Transactional
    public void deleteAll() {
        this.elementCrud.deleteAll();

    }

    @Override
    @Transactional
    public void deleteByKey(ElementKey key) {
        this.elementCrud.deleteById(key);

    }

    @Override
    @Transactional
    public void delete(ElementEntity elementEntity) {
        this.elementCrud.delete(elementEntity);

    }

    @Override
    public List<ElementEntity> readAll(int size, int page) {
        return elementCrud.findAll(PageRequest.of(page, size)).getContent();
    }
}

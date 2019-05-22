package smartspace.dao.nonrdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.*;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;


@Repository
public class nonRdbElementDao implements EnhancedElementDao<ElementKey> {
    private NRdbElementCrud elementCrud;
    private IdGeneratorCrud idGeneratorCrud;
    private MongoTemplate mongoTemplate;

    private final String NAME = "name";



    @Autowired
    public nonRdbElementDao(NRdbElementCrud elementCrud, IdGeneratorCrud idGeneratorCrud, MongoTemplate mongoTemplate) {
        this.idGeneratorCrud = idGeneratorCrud;
        this.elementCrud = elementCrud;
        this.mongoTemplate =mongoTemplate;


    }

    @Override
    @Transactional
    public ElementEntity create(ElementEntity element) {
        if(element.getElementKey() == null) {
            //SMART SPACE DOESN'T EXIST - CREATE NEW KEY
            ElementKey elementKey = new ElementKey();
            elementKey.setElementId(this.idGeneratorCrud.save(new IdGenerator()).getNextId());
            element.setElementKey(elementKey);
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
//        return elementCrud.findAll(PageRequest.of(page, size)).getContent();
        return elementCrud.findAll(
                PageRequest.of(
                        page,
                        size,
                        ASC,
                        NAME))
                .getContent();
    }

    @Override
    public List<ElementEntity> readAllByName(String name, int size, int page) {
        return elementCrud.findAllByName(name, PageRequest.of(page, size));
    }

    @Override
    public List<ElementEntity> readAllWithinLocation(Circle circle) {
        return mongoTemplate.find(new Query(Criteria.where("location").within(circle)), ElementEntity.class);
    }

    @Override
    public List<ElementEntity> readAllByType(String type, int size, int page) {
        return elementCrud.findAllByType(type, PageRequest.of(page, size));
    }
}

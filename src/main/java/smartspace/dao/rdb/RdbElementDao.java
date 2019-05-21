package smartspace.dao.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.ElementDao;
import smartspace.dao.IdGenerator;
import smartspace.dao.IdGeneratorCrud;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RdbElementDao implements ElementDao<ElementKey> {
    private ElementCrud elementCrud;
    private IdGeneratorCrud idGeneratorCrud;

    @Autowired
    public RdbElementDao(ElementCrud elementCrud, IdGeneratorCrud idGeneratorCrud) {
        this.idGeneratorCrud = idGeneratorCrud;
        this.elementCrud = elementCrud;

    }

    @Override
    @Transactional
    public ElementEntity create(ElementEntity element) {
//        IdGenerator idGenerator = new IdGenerator();
//        element.setKey(new ElementKey(this.idGeneratorCrud.save(idGenerator).getNextId()));
//        return this.elementCrud.save(element);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElementEntity> readAll() {
        List<ElementEntity> rv = new ArrayList<>();
        this.elementCrud
                .findAll()
                .forEach(element -> rv.add(element));
        return rv;
    }

    @Override
    public Optional<ElementEntity> readById(ElementKey key) {
        return Optional.empty();
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
        if (this.elementCrud.existsById(update.getKey())) {
            this.elementCrud.save(update);
        } else {
            throw new RuntimeException("no element with id: " + update.getKey());
        }
    }

    @Override
    public List<ElementEntity> readAllWithinLocation(Circle circle) {
        return null;
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

}

package smartspace.dao;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.List;
import java.util.Optional;


public interface ElementDao<K> {

		ElementEntity create(ElementEntity element);

		//SELECT - Read

		List<ElementEntity> readAll();
		Optional<ElementEntity> readById(K key);

		//UPDATE - Update

		void update (ElementEntity update);

		//DELETE - Delete
		void deleteAll();
		void delete(ElementEntity elementEntity);
		void deleteByKey(ElementKey key);


}

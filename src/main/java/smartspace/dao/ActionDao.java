package smartspace.dao;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

import java.util.List;
import java.util.Optional;


public interface ActionDao {

		ActionEntity create(ActionEntity action);
		List<ActionEntity> readAll();
		void deleteAll();


}

package smartspace.dao.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.ActionDao;
import smartspace.dao.IdGeneratorCrud;
import smartspace.data.ActionEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RdbActionDao implements ActionDao {
	private ActionCrud actionCrud;


	@Autowired
	public RdbActionDao(ActionCrud actionCrud, IdGeneratorCrud idGeneratorCrud) {
		this.actionCrud = actionCrud;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity action) {
		return this.actionCrud.save(action);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll() {
		// SELECT ID, text FROM MESSAGES ORDERY BY ID
		List<ActionEntity> rv = new ArrayList<>();
		this.actionCrud
		.findAll()
		.forEach(element->rv.add(element));
		return rv;
	}



	@Override
	@Transactional
	public void deleteAll() {
		this.actionCrud.deleteAll();
	}


}

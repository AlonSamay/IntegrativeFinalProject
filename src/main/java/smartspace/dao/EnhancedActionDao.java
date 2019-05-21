package smartspace.dao;

import smartspace.data.ActionEntity;

import java.util.List;

public interface EnhancedActionDao extends ActionDao{
    public List<ActionEntity> readAll (int size, int page);

}

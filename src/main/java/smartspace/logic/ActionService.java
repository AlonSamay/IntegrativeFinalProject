package smartspace.logic;

import smartspace.data.ActionEntity;

public interface ActionService<K> extends ServicePattern<K>{

    public ActionEntity invoke(ActionEntity actionEntity);


}

package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.ActionDao;
import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;

@Component
public class CheckInPlugin extends ActionPlugIn {

    @Autowired
    public CheckInPlugin(ActionDao actionDao, ObjectMapper jackson) {
        super(actionDao, jackson);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        System.out.println("*******invoke mwthod in checkIn plugin");
        //TODO
        return actionEntity;
    }
}

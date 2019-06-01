package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.ActionDao;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.nonrdb.nonRdbActionDao;
import smartspace.dao.nonrdb.nonRdbElementDao;
import smartspace.data.ActionEntity;

@Component
public class CheckInPlugin extends ActionPlugIn {

    @Autowired
    public CheckInPlugin(nonRdbActionDao actionDao, ObjectMapper jackson, nonRdbElementDao elementDao) {
        super(actionDao, jackson,elementDao);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        System.out.println("*******invoke mwthod in checkIn plugin");
        //TODO
        return actionEntity;
    }
}

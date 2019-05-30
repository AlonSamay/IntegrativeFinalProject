package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;

@Component
public class CheckOutPlugIn extends ActionPlugIn implements PluginCommand{

    @Autowired
    public CheckOutPlugIn(EnhancedActionDao actionDao, ObjectMapper jackson) {
        super(actionDao, jackson);
    }

    @Override
    public ActionEntity invoke(ActionEntity actionEntity) {
        System.out.println("****** invoke method checkout plugin");
        return null;
    }
}

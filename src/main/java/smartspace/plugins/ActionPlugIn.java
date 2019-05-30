package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.ActionDao;
import smartspace.dao.EnhancedActionDao;

//@Component
public abstract class ActionPlugIn implements PluginCommand {

    private ActionDao actionDao;
    private ObjectMapper jackson;

//    @Autowired
    public ActionPlugIn(ActionDao actionDao, ObjectMapper jackson) {
        this.actionDao = actionDao;
        this.jackson = jackson;
    }

}

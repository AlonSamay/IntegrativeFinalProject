package smartspace.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.dao.EnhancedActionDao;

//@Component
public class ActionPlugIn {

    private EnhancedActionDao actionDao;
    private ObjectMapper jackson;

//    @Autowired
    public ActionPlugIn(EnhancedActionDao actionDao, ObjectMapper jackson) {
        this.actionDao = actionDao;
        this.jackson = jackson;
    }

}

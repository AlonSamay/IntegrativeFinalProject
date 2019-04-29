package smartspace.layout;

import smartspace.data.ActionEntity;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ActionBoundary {

    private Map<String,String> actionKey;
    private String type;
    private Date created;
    private Map<String,String> element;
    private Map<String,String> player;
    private Map<String,Object> properties;



    public ActionBoundary(){
    }
    public ActionBoundary(ActionEntity entity){
        this.actionKey = new TreeMap<>();
        this.actionKey.put("id",entity.getActionId());
        this.actionKey.put("smartspace",entity.getActionSmartSpace());

        this.type = entity.getActionType();
        this.created = entity.getCreationTimeStamp();

        this.element = new TreeMap<>();
        this.element.put("id",entity.getElementId());
        this.element.put("smartspace",entity.getActionSmartSpace());

        this.player = new TreeMap<>();
        this.player.put("smartspace",entity.getActionSmartSpace());
        this.player.put("email",entity.getPlayerEmail());

        this.properties = entity.getMoreAttributes();
    }
    public  ActionEntity convertToEntity(){
        ActionEntity entity = new ActionEntity();

        if(this.actionKey != null && this.actionKey.get("id") !=null && this.actionKey.get("smartspace") != null){
            entity.setKey(this.actionKey.get("id"));
            entity.setActionSmartSpace(this.actionKey.get("smartspace"));
        }

        entity.setActionType(this.type);
        entity.setCreationTimeStamp(this.created);

        if(this.element != null && this.element.get("id") !=null && this.element.get("smartspace") != null) {
            entity.setActionId(this.element.get("id"));
            entity.setElementSmartSpace(this.element.get("smartspace"));
        }
        if(this.player != null && this.player.get("id") !=null && this.player.get("smartspace") != null) {
            entity.setActionId(this.player.get("id"));
            entity.setElementSmartSpace(this.player.get("smartspace"));
        }

        entity.setMoreAttributes(new TreeMap<String, Object>());
        if(this.properties != null){
            entity.setMoreAttributes(this.properties);
        }
        return entity;
    }
    public Map<String, String> getActionKey() {
        return actionKey;
    }

    public void setActionKey(Map<String, String> actionKey) {
        this.actionKey = actionKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Map<String, String> getElement() {
        return element;
    }

    public void setElement(Map<String, String> element) {
        this.element = element;
    }

    public Map<String, String> getPlayer() {
        return player;
    }

    public void setPlayer(Map<String, String> player) {
        this.player = player;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

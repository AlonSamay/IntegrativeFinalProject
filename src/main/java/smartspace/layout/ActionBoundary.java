package smartspace.layout;

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import smartspace.data.ElementKey;
import smartspace.data.UserKey;

import java.util.Date;
import java.util.Map;

public class ActionBoundary {

    private ActionKey actionKey;
    private String type;
    private Date created;
    private ElementKey element;
    private UserKey player;
    private Map<String, Object> properties;

    public ActionBoundary() {
    }

    public ActionBoundary(ActionEntity entity) {

        this.actionKey = new ActionKey(entity.getActionId(),entity.getActionSmartSpace());


        this.type = entity.getActionType();

        this.created = entity.getCreationTimeStamp();


        this.element = new ElementKey(entity.getElementId(),entity.getElementSmartSpace());

        this.player= new UserKey(entity.getPlayerEmail(),entity.getElementSmartSpace());

        this.properties = entity.getMoreAttributes();
    }

    public ActionEntity convertToEntity() {
        ActionEntity entity = new ActionEntity();

        if (this.actionKey !=null ) {
            entity.setKey(this.actionKey.getActionId());
            entity.setActionSmartSpace(this.actionKey.getActionSmartSpace());
        }
        else
            entity.setActionSmartSpace(new ActionKey().getActionSmartSpace());

        entity.setActionType(this.type);

        entity.setCreationTimeStamp(this.created);

        entity.setElementId(this.element.getId());
        entity.setElementSmartSpace(this.element.getSmartspace());

        entity.setPlayerEmail(this.player.getEmail());
        entity.setPlayerSmartSpace(this.player.getSmartspace());

        if (this.properties != null) {
            entity.setMoreAttributes(this.properties);
        } else {
            entity.setMoreAttributes(null);
        }

        return entity;
    }

    public ActionKey getActionKey() {
        return actionKey;
    }

    public void setActionKey(ActionKey actionKey) {
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

    public ElementKey getElement() {
        return element;
    }

    public void setElement(ElementKey element) {
        this.element = element;
    }

    public UserKey getPlayer() {
        return player;
    }

    public void setPlayer(UserKey player) {
        this.player = player;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

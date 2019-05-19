package smartspace.layout;

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import smartspace.data.ElementKey;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ActionBoundary {
    private static final String ID = "id";
    private static final String SMARTSPACE = "smartspace";
    private static final String EMAIL = "email";

    private ActionKey actionKey;
    private String type;
    private Date created;
    private ElementKey element;
    private Map<String, String> player;
    private Map<String, Object> properties;

    public ActionBoundary() {
    }

    public ActionBoundary(ActionEntity entity) {
//        this.actionKey = new TreeMap<>();
//        this.actionKey.put(ID, entity.getActionId());
//        this.actionKey.put(SMARTSPACE, entity.getActionSmartSpace());
//        this.actionKey.put(SMARTSPACE, "talCohenB");

        this.actionKey = new ActionKey(entity.getActionId(),entity.getActionSmartSpace());


        this.type = entity.getActionType();

        this.created = entity.getCreationTimeStamp();

//        this.element = new TreeMap<>();
//        this.element.put(ID, entity.getElementId());
//        this.element.put(SMARTSPACE, entity.getElementSmartSpace());

        this.element = new ElementKey(entity.getElementId(),entity.getElementSmartSpace());



        this.player = new TreeMap<>();
        this.player.put(SMARTSPACE, entity.getPlayerSmartSpace());
        this.player.put(EMAIL, entity.getPlayerEmail());

        this.properties = entity.getMoreAttributes();
    }

    public ActionEntity convertToEntity() {
        ActionEntity entity = new ActionEntity();

        entity.setKey(this.actionKey.getActionId());
        entity.setActionSmartSpace(this.actionKey.getActionSmartSpace());

        entity.setActionType(this.type);

        entity.setCreationTimeStamp(this.created);

        entity.setElementId(this.element.getElementId());
        entity.setElementSmartSpace(this.element.getElementSmartSpace());

        entity.setPlayerEmail(this.player.get(EMAIL));
        entity.setPlayerSmartSpace(this.player.get(SMARTSPACE));

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

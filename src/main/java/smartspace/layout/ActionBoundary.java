package smartspace.layout;

import smartspace.data.ActionEntity;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ActionBoundary {
    private static final String ID = "id";
    private static final String SMARTSPACE = "smartspace";
    private static final String EMAIL = "email";

    private Map<String, String> actionKey;
    private String type;
    private Date created;
    private Map<String, String> element;
    private Map<String, String> player;
    private Map<String, Object> properties;

    public ActionBoundary() {
    }

    public ActionBoundary(ActionEntity entity) {
        this.actionKey = new TreeMap<>();
        this.actionKey.put(ID, entity.getActionId());
        this.actionKey.put(SMARTSPACE, entity.getActionSmartSpace());

        this.type = entity.getActionType();

        this.created = entity.getCreationTimeStamp();

        this.element = new TreeMap<>();
        this.element.put(ID, entity.getElementId());
        this.element.put(SMARTSPACE, entity.getElementSmartSpace());

        this.player = new TreeMap<>();
        this.player.put(SMARTSPACE, entity.getPlayerSmartSpace());
        this.player.put(EMAIL, entity.getPlayerEmail());

        this.properties = entity.getMoreAttributes();
    }

    public ActionEntity convertToEntity() {
        ActionEntity entity = new ActionEntity();

        if (this.actionKey != null && this.actionKey.get(ID) != null && this.actionKey.get(SMARTSPACE) != null) {
            entity.setKey(this.actionKey.get(ID));
            entity.setActionSmartSpace(this.actionKey.get(SMARTSPACE));
        }

        entity.setActionType(this.type);

        entity.setCreationTimeStamp(this.created);

        if (this.element != null && this.element.get(ID) != null && this.element.get(SMARTSPACE) != null) {
            entity.setElementId(this.element.get(ID));
            entity.setElementSmartSpace(this.element.get(SMARTSPACE));
        }

        if (this.player != null && this.player.get(EMAIL) != null && this.player.get(SMARTSPACE) != null) {
            entity.setPlayerEmail(this.player.get(EMAIL));
            entity.setPlayerSmartSpace(this.player.get(SMARTSPACE));
        }

        if (this.properties != null && !properties.isEmpty()) {
            entity.setMoreAttributes(this.properties);
        } else {
            entity.setMoreAttributes(null);
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

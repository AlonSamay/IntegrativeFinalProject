package smartspace.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "ACTIONS")

public class ActionEntity implements SmartspaceEntity<ActionKey> {

    private String elementSmartSpace;
    private String elementId;
    private String playerSmartSpace;
    private String playerEmail;
    private String actionType;
    private Date creationTimeStamp;
    private Map<String, Object> moreAttributes;

    @Id
    private ActionKey actionKey;

    public ActionEntity() {
        this.moreAttributes = new HashMap<>();
    }

    public ActionEntity(String elementId, String elementSmartSpace, String actionType, Date creationTimeStamp, String playerEmail, String playerSmartSpace, Map<String, Object> moreAttributes) {
        this.elementId = elementId;
        this.elementSmartSpace = elementSmartSpace;
        this.actionType = actionType;
        this.creationTimeStamp = creationTimeStamp;
        this.playerEmail = playerEmail;
        this.playerSmartSpace = playerSmartSpace;
        this.moreAttributes = moreAttributes;
        this.actionKey = new ActionKey();
    }

    public void setElementSmartSpace(String elementSmartSpace) {
        this.elementSmartSpace = elementSmartSpace;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public void setPlayerSmartSpace(String playerSmartSpace) {
        this.playerSmartSpace = playerSmartSpace;
    }

    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setCreationTimeStamp(Date creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public void setMoreAttributes(Map<String, Object> moreAttributes) {
        this.moreAttributes = moreAttributes;
    }


    public String getActionId() {
        return this.actionKey.getId();
    }

    public void setActionId(String actionId) {
        this.actionKey.setId(actionId);
    }


    public String getElementSmartSpace() {
        return this.elementSmartSpace;
    }


    public String getElementId() {
        return this.elementId;
    }


    public String getPlayerSmartSpace() {
        return this.playerSmartSpace;
    }


    public String getPlayerEmail() {
        return this.playerEmail;
    }


    public String getActionType() {
        return actionType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationTimeStamp() {
        return creationTimeStamp;
    }

    @Lob
    //@Convert(converter= MapToJsonConverter.class)
    public Map<String, Object> getMoreAttributes() {
        return moreAttributes;
    }


    public void setActionSmartSpace(String actionSmartSpace) {
        this.actionKey.setSmartspace(actionSmartSpace);
    }

    public String getActionSmartSpace() {
        return this.actionKey.getSmartspace();
    }

    @Override
    public ActionKey getKey() {
        return this.actionKey;
    }


    @Override
    public void setKey(ActionKey key) {
        this.actionKey = key;
    }

    @Override
    public String toString() {
        return "ActionEntity{" +
                "actionSmartSpace='" + actionKey.getSmartspace() + '\'' +
                ", actionId='" + actionKey.getId() + '\'' +
                ", elementSmartSpace='" + elementSmartSpace + '\'' +
                ", elementId='" + elementId + '\'' +
                ", playerSmartSpace='" + playerSmartSpace + '\'' +
                ", playerEmail='" + playerEmail + '\'' +
                ", actionType='" + actionType + '\'' +
                ", creationTimeStamp=" + creationTimeStamp +
                ", moreAttributes=" + moreAttributes +
                '}';
    }
}

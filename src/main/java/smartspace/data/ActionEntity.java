package smartspace.data;

import org.springframework.data.mongodb.core.mapping.Document;
import smartspace.dao.rdb.MapToJsonConverter;

import javax.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection="ACTIONS")
public class ActionEntity implements SmartspaceEntity<String> {

    private String actionSmartSpace;
    private String actionId;
    private String elementSmartSpace;
    private String elementId;
    private String playerSmartSpace;
    private String playerEmail;
    private String actionType;
    private Date   creationTimeStamp;
    private Map<String,Object> moreAttributes;

    public ActionEntity() {
        this.moreAttributes = new HashMap<>();
    }

    public ActionEntity(String elementId, String elementSmartSpace, String actionType, Date creationTimeStamp, String playerEmail, String playerSmartSpace,  Map<String, Object> moreAttributes) {
        this.elementId = elementId;
        this.elementSmartSpace = elementSmartSpace;
        this.actionType = actionType;
        this.creationTimeStamp = creationTimeStamp;
        this.playerEmail = playerEmail;
        this.playerSmartSpace = playerSmartSpace;
        this.moreAttributes = moreAttributes;
    }

    public void setActionSmartSpace(String actionSmartSpace) {
        this.actionSmartSpace = actionSmartSpace;
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

    @Id
    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionSmartSpace() {
        return actionSmartSpace;
    }

    public String getElementSmartSpace() {
        return elementSmartSpace;
    }


    public String getElementId() {
        return elementId;
    }


    public String getPlayerSmartSpace() {
        return playerSmartSpace;
    }


    public String getPlayerEmail() {
        return playerEmail;
    }


    public String getActionType() {
        return actionType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationTimeStamp() {
        return creationTimeStamp;
    }

    @Lob
	@Convert(converter= MapToJsonConverter.class)
    public Map<String, Object> getMoreAttributes() {
        return moreAttributes;
    }

//    @Column(name="ID")

//    @GeneratedValue

    @Override
    public String getKey() {
        return this.actionId;
    }

    @Override
    public void setKey(String key) {
        this.actionId = key;
    }

    @Override
    public String toString() {
        return "ActionEntity{" +
                "actionSmartSpace='" + actionSmartSpace + '\'' +
                ", actionId='" + actionId + '\'' +
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

package smartspace.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Document(collection="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<ElementKey> {

    private Location location;
    private String name;
    private String type;
    private Date creationTimeStamp;
    private boolean expired;
    private String creatorSmartSpace;
    private String creatorEmail;
    private Map<String, Object> moreAttributes;

    @Id
    private ElementKey elementKey;

    public ElementEntity() {
        this.moreAttributes = new HashMap<>();

    }

    public ElementEntity(String name, String type, Location location, Date creationTimeStamp, String creatorEmail, String creatorSmartSpace, boolean expired, Map<String, Object> moreAttributes) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.creationTimeStamp = creationTimeStamp;
        this.creatorEmail = creatorEmail;
        this.creatorSmartSpace = creatorSmartSpace;
        this.expired = expired;
        this.moreAttributes = moreAttributes;
    }

    public String getElementSmartSpace(){
        return this.elementKey.getElementSmartSpace();
    }

    public void SetElementSmartSpace(String elementSmartSpace){
        this.elementKey.setElementSmartSpace(elementSmartSpace);
    }

    public String getElementId(){
        return this.elementKey.getElementId();
    }

    public void SetElementId(String elementId){
        this.elementKey.setElementId(elementId);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreationTimeStamp(Date creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public void setCreatorSmartSpace(String creatorSmartSpace) {
        this.creatorSmartSpace = creatorSmartSpace;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public void setMoreAttributes(Map<String, Object> moreAttributes) {
        this.moreAttributes = moreAttributes;
    }


    public ElementKey getElementKey() {
        return elementKey;
    }

    public void setElementKey(ElementKey elementKey) {
        this.elementKey = elementKey;
    }

    //    @Embedded
    public Location getLocation() {
        return location;
    }


    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationTimeStamp() {
        return creationTimeStamp;
    }


    public boolean getExpired() {
        return expired;
    }


    public String getCreatorSmartSpace() {
        return creatorSmartSpace;
    }


    public String getCreatorEmail() {
        return creatorEmail;
    }

    @Lob
    //@Convert(converter= MapToJsonConverter.class)
    public Map<String, Object> getMoreAttributes() {
        return moreAttributes;
    }

    @Override
    public ElementKey getKey() {
        return this.elementKey;
    }


    @Override
    public void setKey(ElementKey key) {
        this.elementKey= key;
    }






    @Override
    public String toString() {
        return "ElementEntity{" +
                ", location=" + location +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", creationTimeStamp=" + creationTimeStamp +
                ", expired=" + expired +
                ", creatorSmartSpace='" + creatorSmartSpace + '\'' +
                ", creatorEmail='" + creatorEmail + '\'' +
                ", moreAttributes=" + moreAttributes +
                ", elementKey=" + elementKey +
                '}';
    }
}

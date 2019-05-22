package smartspace.layout;

import smartspace.data.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ElementBoundary {
    private static final String ID = "id";
    private static final String SMARTSPACE = "smartspace";
    private static final String EMAIL = "email";
    private static final String LAT = "lat";
    private static final String LNG = "lng";

    private ElementKey key;
    private String elementType;
    private String name;
    private Boolean expired;
    private Timestamp created;
    private UserKey creator;
    private Map<String, Double> latlng;
    private Map<String, Object> elementProperties;

    public ElementBoundary() {
    }

    public ElementBoundary(ElementEntity entity) {
//        this.key = new TreeMap<>();
//        this.key.put(ID, entity.getKey().getElementId());
//        this.key.put(SMARTSPACE, entity.getKey().getElementSmartSpace());

        this.key = new ElementKey(entity.getKey().getElementId(),entity.getKey().getElementSmartSpace());

        this.elementType = entity.getType();

        this.name = entity.getName();

        this.expired = entity.getExpired();

        this.created = new Timestamp(entity.getCreationTimeStamp().getTime());

//        this.creator = new TreeMap<>();
//        this.creator.put(EMAIL, entity.getCreatorEmail());
//        this.creator.put(SMARTSPACE, entity.getCreatorSmartSpace());

        this.creator = new UserKey(new MailAdress(entity.getCreatorEmail()));

        this.latlng = new TreeMap<>();
        this.latlng.put(LAT, entity.getLocation().getX());
        this.latlng.put(LNG, entity.getLocation().getY());

        this.elementProperties = entity.getMoreAttributes();
    }

    public ElementEntity convertToEntity() {
        ElementEntity entity = new ElementEntity();

        if(this.key == null){
            entity.setKey(null);
        }
        else{
            ElementKey key = new ElementKey(this.key.getElementId(),this.key.getElementSmartSpace());
            entity.setKey(key);
        }

        if (this.latlng.get(LAT) != null && this.latlng.get(LNG) != null) {
            entity.setLocation(new Location(this.latlng.get(LAT), this.latlng.get(LNG)));
        }

        entity.setName(this.name);

        entity.setType(this.elementType);

        entity.setCreationTimeStamp(this.created);

        entity.setExpired(this.expired);

        if (this.creator.getEmail() != null && this.creator.getId() != null) {
            entity.setCreatorSmartSpace(this.creator.getId());
            entity.setCreatorEmail(this.creator.getEmail().getMail());
        }

        entity.setMoreAttributes(this.elementProperties);

        return entity;
    }

    public ElementKey getKey() {
        return key;
    }

    public void setKey(ElementKey key) {
        this.key = key;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public UserKey getCreator() {
        return creator;
    }

    public void setCreator(UserKey creator) {
        this.creator = creator;
    }

    public Map<String, Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(Map<String, Double> latlng) {
        this.latlng = latlng;
    }

    public Map<String, Object> getElementProperties() {
        return elementProperties;
    }

    public void setElementProperties(Map<String, Object> elementProperties) {
        this.elementProperties = elementProperties;
    }
}

package smartspace.layout;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

public class ElementBoundary {

    private Map<String,String> key;
    private String elementType;
    private String name;
    private Boolean expired;
    private Timestamp created;
    private Map<String,String> creator;
    private Map<String, Double> latlng;
    private Map<String,Object> elementProperties;




    public ElementBoundary(){
    }
//
    public ElementBoundary(ElementEntity entity){
        this.key = new TreeMap<>();
        this.key.put("id",entity.getKey().getElementId());
        this.key.put("smartspace",entity.getKey().getElementSmartSpace());

        this.elementType = entity.getType();
        this.name = entity.getName();
        this.created = (Timestamp) entity.getCreationTimeStamp();
        this.creator = new TreeMap<>();
        this.creator.put("email",entity.getCreatorEmail());
        this.creator.put("smartspace",entity.getKey().getElementSmartSpace());

        this.latlng = new TreeMap<>();
        this.latlng.put("lat",entity.getLocation().getX());
        this.latlng.put("lng",entity.getLocation().getY());

        this.elementProperties = entity.getMoreAttributes();
    }
    public ElementEntity convertToEntity() {
        ElementEntity entity = new ElementEntity();

        if (!this.key.isEmpty() && this.key.get("id") != null && this.key.get("smartspace") != null) {
            entity.setKey(new ElementKey(this.key.get("id")));
//            entity.setElementSmartSpace(this.key.get("smartspace"));  //TODO: need to fix
        }
        if (!this.latlng.isEmpty() && this.latlng.get("lat") != null && this.latlng.get("lng") != null) {
            entity.setLocation(new Location(this.latlng.get("lat"), this.latlng.get("lng")));
        }

        entity.setName(this.name);
        entity.setType(this.elementType);
        entity.setCreationTimeStamp(this.created);
        entity.setExpired(this.expired);

        if (!this.creator.isEmpty() && this.creator.get("email") != null && this.creator.get("smartspace") != null) {
            entity.setCreatorSmartSpace(this.creator.get("smartspace"));
            entity.setCreatorEmail(this.creator.get("email"));
        }

        entity.setMoreAttributes(this.elementProperties);

        return entity;
    }
    public Map<String, String> getKey() {
        return key;
    }

    public void setKey(Map<String, String> key) {
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Map<String, String> getCreator() {
        return creator;
    }

    public void setCreator(Map<String, String> creator) {
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

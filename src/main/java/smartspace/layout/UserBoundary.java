package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

import java.util.Map;
import java.util.TreeMap;

public class UserBoundary {

    private Map<String,String> key;
    private UserRole role;
    private String username;
    private String avatar;
    private Long points;

    public UserBoundary(){

    }
    public UserBoundary(UserEntity entity){

        this.key = new TreeMap<>();
        this.key.put("id",entity.getUserSmartSpace());
        this.key.put("email",entity.getKey().getEmail());

        this.role = entity.getRole();
        this.username = entity.getUsername();
        this.avatar = entity.getAvatar();
        this.points = entity.getPoints();
    }
    
    public UserEntity convertToEntity(){
        UserEntity entity = new UserEntity();
        if(this.key != null && this.key.get("id") !=null && this.key.get("email") != null){
//            entity.setKey(new UserKey(this.key.get("id"),this.key.get("email"))); //TODO: need to fix
        }
        entity.setRole(this.role);

        if (entity.getUsername() != null) {
            this.username = entity.getUsername().replace("#"," ");// ask oren how space is shown in db
        } else {
            this.username = null;
        }

        entity.setAvatar(this.avatar);
        entity.setPoints(this.points);
        return entity;
    }

    public Map<String, String> getKey() {
        return key;
    }

    public void setKey(Map<String, String> key) {
        this.key = key;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}

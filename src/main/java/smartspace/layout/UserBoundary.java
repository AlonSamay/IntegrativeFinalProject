package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

import java.util.Map;
import java.util.TreeMap;

public class UserBoundary {
    private static final String SMARTSPACE = "smartspace";
    private static final String EMAIL = "email";

    private Map<String, String> key;
    private String role;
    private String username;
    private String avatar;
    private Long points;

    public UserBoundary() {

    }

    // TODO: check which strings might have spaces in it. replace any space with #
    public UserBoundary(UserEntity entity) {
        this.key = new TreeMap<>();
        this.key.put(SMARTSPACE, entity.getUserSmartSpace());
        this.key.put(EMAIL, entity.getKey().getEmail());

        this.role = entity.getRole().name();

        this.username = entity.getUsername();

        if (entity.getAvatar() != null) {
            this.avatar = entity.getAvatar().replaceAll(" ", "#");
        } else {
            this.avatar = null;
        }

        this.points = entity.getPoints();
    }

    public UserEntity convertToEntity() {
        UserEntity entity = new UserEntity();

        if (this.key != null && this.key.get(EMAIL) != null) {
            entity.setKey(new UserKey(this.key.get(EMAIL)));
        }

        entity.setRole(UserRole.valueOf(this.role));

        entity.setUsername(this.username);

        entity.setAvatar(this.avatar.replaceAll("#", " "));

        entity.setPoints(this.points);

        return entity;
    }

    public Map<String, String> getKey() {
        return key;
    }

    public void setKey(Map<String, String> key) {
        this.key = key;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

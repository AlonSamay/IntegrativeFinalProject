package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

public class UserBoundary {
    private static final String SMARTSPACE = "smartspace";
    private static final String EMAIL = "email";

    private UserKey key;
    private String role;
    private String username;
    private String avatar;
    private Long points;

    public UserBoundary() {

    }

    public UserBoundary(UserEntity entity) {
//        this.key = new TreeMap<>();
////        this.key.put(SMARTSPACE, entity.getKey().getSmartspace());
////        this.key.put(EMAIL, entity.getKey().getEmail());

        this.key = new UserKey(entity.getKey().getSmartspace(),entity.getKey().getEmail());

        this.role = entity.getRole().name();

        this.username = entity.getUsername();
        this.avatar = entity.getAvatar();

        this.points = entity.getPoints();
    }

    public UserEntity convertToEntity() {
        UserEntity entity = new UserEntity();

        if (this.key != null && this.key.getEmail() != null && this.key.getSmartspace() != null) {
            UserKey key = new UserKey();
            key.setEmail(this.key.getEmail());
            key.setSmartSpace(this.key.getSmartspace());
            entity.setKey(key);
        }

        entity.setRole(this.role != null ? UserRole.valueOf(this.role) : null);

        entity.setUsername(this.username);

        entity.setAvatar(this.avatar);

        entity.setPoints(this.points);

        return entity;
    }

    public UserKey getKey() {
        return key;
    }

    public void setKey(UserKey key) {
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
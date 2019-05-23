package smartspace.layout;

import smartspace.data.EmailAddress;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

public class UserBoundary {
    private UserKey key;
    private String role;
    private String username;
    private String avatar;
    private Long points;

    public UserBoundary() {

    }

    public UserBoundary(UserEntity entity) {
        this.key = entity.getKey();

        this.role = entity.getRole().name();

        this.username = entity.getUsername();
        this.avatar = entity.getAvatar();

        this.points = entity.getPoints();
    }

    public UserEntity convertToEntity() {
        UserEntity entity = new UserEntity();

        entity.setKey(key);

        entity.setRole(this.role != null ? UserRole.valueOf(this.role) : null);

        entity.setUsername(this.username);

        entity.setAvatar(this.avatar);
        if(this.points ==null)
         entity.setPoints(0L);
        else
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
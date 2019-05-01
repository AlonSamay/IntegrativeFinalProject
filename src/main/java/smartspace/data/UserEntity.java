package smartspace.data;

import org.hibernate.annotations.SQLInsert;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.persistence.*;


@Document(collection="USERS")
public class UserEntity implements SmartspaceEntity<UserKey> {

    private String username;
    private String avatar;
    private UserRole role;
    private Long points;

    @Id
    private UserKey userKey;

    public UserEntity(String userName, String avatar, UserRole role, Long points) {
        this.username = userName;
        this.avatar = avatar;
        this.role = role;
        this.points = points;
    }

    public UserEntity() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }


    public String getAvatar() {
        return avatar;
    }

    @Enumerated(EnumType.STRING)
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    public Long getPoints() {
        return points;
    }

    public UserKey getUserKey() {
        return userKey;
    }

    public void setUserKey(UserKey userKey) {
        this.userKey = userKey;
    }

    @Override
    public UserKey getKey() {
        return this.userKey;
    }

    @Override
    public void setKey(UserKey key) {
        this.userKey = key;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role=" + role +
                ", points=" + points +
                ", userKey=" + userKey +
                '}';
    }
}

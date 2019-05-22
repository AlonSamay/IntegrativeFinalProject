package smartspace.layout;

import smartspace.data.MailAdress;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

public class UserForm {
    private String email;
    private String role;
    private String username;
    private String avatar;

    public UserForm() {
    }

    public UserForm(String email, String role, String username, String avatar) {
        this.email = email;
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }

    public UserEntity convertToEntity() {
        UserEntity entity = new UserEntity();

        entity.setKey(new UserKey(new MailAdress(this.email)));
        entity.setUsername(this.username);
        entity.setRole(UserRole.valueOf(this.role));
        entity.setAvatar(this.avatar);
        entity.setPoints((long) 100);

        return entity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

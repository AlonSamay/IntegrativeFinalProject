package smartspace.data;

import java.io.Serializable;
import java.util.Objects;


public class UserKey implements Serializable, Comparable<String> {

    private static final long serialVersionUID = 8656507240646501250L;

    private String smartspace;
    private String email;

    public UserKey() {
    }

    public UserKey(String email) {
        this.smartspace = "2019BTal.Cohen";
        this.email = email;
    }

    public UserKey(String email, String smartspace) {
        this.smartspace = smartspace;
        this.email = email;
    }

    public String getSmartspace() {
        return smartspace;
    }

    public String getEmail() {
        return email;
    }

    public void setSmartspace(String smartspace) {
        this.smartspace = smartspace;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserKey)) return false;
        UserKey userKey = (UserKey) o;
        return Objects.equals(smartspace, userKey.smartspace) &&
                Objects.equals(email, userKey.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartspace, email);
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }

    @Override
    public String toString() {
        return "UserKey{" +
                "smartspace='" + smartspace + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

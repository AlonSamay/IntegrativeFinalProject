package smartspace.data;
import java.io.Serializable;
import java.util.Objects;


public class UserKey implements Serializable ,Comparable<String>{

    private static final long serialVersionUID = 8656507240646501250L;

    private String smartSpace;
    private MailAdress email;

    public UserKey() {


    }

    public UserKey(MailAdress email) {
        this.smartSpace = "2019BTal.Cohen";
        this.email = email;
    }

    public UserKey(MailAdress email, String smartSpace) {
        this.smartSpace = smartSpace;
        this.email = email;
    }

    public String getId() {
        return smartSpace;
    }

    public MailAdress getEmail() {
        return email;
    }

    public void setId(String smartspace) {
        this.smartSpace = smartspace;
    }

    public void setEmail(MailAdress email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserKey)) return false;
        UserKey userKey = (UserKey) o;
        return Objects.equals(smartSpace, userKey.smartSpace) &&
                Objects.equals(email, userKey.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartSpace, email);
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }

    @Override
    public String toString() {
        return "UserKey{" +
                "id='" + smartSpace + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

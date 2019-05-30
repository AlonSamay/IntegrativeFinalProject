package smartspace.data;

import java.util.Objects;

public class ActionKey {

    private static final long serialVersionUID = 8665542775633852231L;

    private String smartspace;
    private String id;

    public ActionKey() {
        this.smartspace = "2019BTalCohen";
    }
    public ActionKey(String id) {
        this.id=id;
        this.smartspace = "2019BTalCohen";
    }

    public ActionKey(String id, String actionSmartSpace){
        this.id = id;
        this.smartspace = actionSmartSpace;
    }


    public String getSmartspace() {
        return smartspace;
    }

    public void setSmartspace(String smartspace) {
        this.smartspace = smartspace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionKey)) return false;
        ActionKey that = (ActionKey) o;
        return Objects.equals(smartspace, that.smartspace) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartspace, id);
    }

    @Override
    public String toString() {
        return "ActionKey{" +
                "smartspace='" + smartspace + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

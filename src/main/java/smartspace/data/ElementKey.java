package smartspace.data;

import java.io.Serializable;
import java.util.Objects;

public class ElementKey implements Serializable, Comparable<String> {

    private static final long serialVersionUID = 8665542775633852231L;

    private String smartspace;
    private String id;

    public ElementKey() {
        this.smartspace = "2019BTal.Cohen";
    }

    public ElementKey(String elementId, String elementSmartspace){
        this.id = elementId;
        this.smartspace = elementSmartspace;
    }


    @Override
    public int compareTo(String o) {
        return 0;
    }

    public String getSmartspace() {
        return smartspace;
    }

    public String getId() {
        return id;
    }

    public void setSmartspace(String smartspace) {
        this.smartspace = smartspace;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementKey)) return false;
        ElementKey that = (ElementKey) o;
        return Objects.equals(smartspace, that.smartspace) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartspace, id);
    }

    @Override
    public String toString() {
        return "ElementKey{" +
                "smartspace='" + smartspace + '\'' +
                ", id=" + id +
                '}';
    }
}



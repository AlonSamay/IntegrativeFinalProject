package smartspace.data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.util.Objects;

public class ElementKey implements Serializable, Comparable<String> {

    private static final long serialVersionUID = 8665542775633852231L;

    private String elementSmartSpace;
    private String elementId;

    public ElementKey() {
        this.elementSmartSpace = "2019BTalCohen";
    }

    public ElementKey(String elementId, String elementSmartspace){
        this.elementId = elementId;
        this.elementSmartSpace = elementSmartspace;
    }


    @Override
    public int compareTo(String o) {
//        if (Integer.parseInt(id) > Integer.parseInt(o)) {
//            return 1;
//        } else if (Integer.parseInt(this.id) < Integer.parseInt(o)) {
//            return -1;
//        } else {
//            return 0;
//        }
        return 0;
    }

    public String getElementSmartSpace() {
        return elementSmartSpace;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementSmartSpace(String elementSmartSpace) {
        this.elementSmartSpace = elementSmartSpace;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementKey)) return false;
        ElementKey that = (ElementKey) o;
        return Objects.equals(elementSmartSpace, that.elementSmartSpace) &&
                Objects.equals(elementId, that.elementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementSmartSpace, elementId);
    }

    @Override
    public String toString() {
        return "ElementKey{" +
                "elementSmartSpace='" + elementSmartSpace + '\'' +
                ", elementId=" + elementId +
                '}';
    }
}



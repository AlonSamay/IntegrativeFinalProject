package smartspace.data;

import java.util.Objects;

public class ActionKey {

    private static final long serialVersionUID = 8665542775633852231L;

    private String actionSmartSpace;
    private String actionId;

    public ActionKey() {
        this.actionSmartSpace = "2019BTalCohen";
    }

    public ActionKey(String elementId, String elementSmartspace){
        this.actionId = elementId;
        this.actionSmartSpace = elementSmartspace;
    }


    public String getActionSmartSpace() {
        return actionSmartSpace;
    }

    public void setActionSmartSpace(String actionSmartSpace) {
        this.actionSmartSpace = actionSmartSpace;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionKey)) return false;
        ActionKey that = (ActionKey) o;
        return Objects.equals(actionSmartSpace, that.actionSmartSpace) &&
                Objects.equals(actionId, that.actionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionSmartSpace, actionId);
    }

    @Override
    public String toString() {
        return "ActionKey{" +
                "actionSmartSpace='" + actionSmartSpace + '\'' +
                ", actionId='" + actionId + '\'' +
                '}';
    }
}

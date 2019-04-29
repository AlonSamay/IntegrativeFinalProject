package smartspace.data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;

public interface SmartspaceEntity<K> {
    @Column(name="ID")
    @EmbeddedId
    public K getKey();
    public void setKey (K key);

}

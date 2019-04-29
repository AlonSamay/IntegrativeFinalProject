package smartspace.logic;
import java.util.List;

public interface ServicePattern <K>{
    public List<K> getAll(int size, int page);
    public K store(K entity);
}

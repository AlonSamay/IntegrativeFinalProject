package smartspace.logic;


import java.util.List;

public interface ServicePattern <K>{
     List<K> getAll(int size, int page);
     K store(K entity);
}

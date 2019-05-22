package smartspace.logic;

import java.util.List;

public interface ElementService<K> extends ServicePattern<K>{
    void update(K entity);

    K readById(String smartSpace, String id);

    List<K> getElementsBySearchTerm(String param, String value, double x, double y, double distance, int size, int page);
}

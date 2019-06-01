package smartspace.dao;

import org.springframework.data.geo.Circle;
import smartspace.data.ElementEntity;

import java.util.List;

public interface nonRdbElementDao<k> extends ElementDao<k>{
    public List<ElementEntity> readAll (int size, int page);

    public List<ElementEntity> readAllByName(String name, int size, int page);
    public List<ElementEntity> readAllByType(String type, int size, int page);

    public List<ElementEntity> readAllWithinLocation(Circle circle);

//    //STORE WITHOUT CHANING THE SMARTSPACE
//    ElementEntity store(ElementEntity elementEntity);

}

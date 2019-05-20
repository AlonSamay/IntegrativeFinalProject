package smartspace.dao.nonrdb;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

import java.util.List;


@RepositoryRestResource
public interface NRdbElementCrud extends PagingAndSortingRepository<ElementEntity, ElementKey> {
    List<ElementEntity> findAllByName(String name, Pageable pageable);

    List<ElementEntity> findAllByType(String type, Pageable pageable);
}

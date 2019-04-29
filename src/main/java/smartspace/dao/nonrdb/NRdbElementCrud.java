package smartspace.dao.nonrdb;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;


@RepositoryRestResource
public interface NRdbElementCrud extends PagingAndSortingRepository<ElementEntity, ElementKey> {
}

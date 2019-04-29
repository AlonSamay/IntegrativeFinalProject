package smartspace.dao.nonrdb;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import smartspace.data.ActionEntity;


@RepositoryRestResource
public interface NRdbActionCrud extends PagingAndSortingRepository<ActionEntity, String> {


}

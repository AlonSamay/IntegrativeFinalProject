package smartspace.dao.nonrdb;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@RepositoryRestResource
public interface NUserCrud extends PagingAndSortingRepository<UserEntity, UserKey> {
}

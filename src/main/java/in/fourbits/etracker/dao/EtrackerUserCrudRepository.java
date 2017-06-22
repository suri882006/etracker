package in.fourbits.etracker.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import in.fourbits.etracker.entity.EtrackerUser;

@RepositoryRestResource
public interface EtrackerUserCrudRepository extends CrudRepository<EtrackerUser, Long> {

	public EtrackerUser findByUserName(String username);		
	
}

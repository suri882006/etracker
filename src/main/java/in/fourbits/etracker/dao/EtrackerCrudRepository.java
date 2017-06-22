package in.fourbits.etracker.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import in.fourbits.etracker.entity.Expense;

@RepositoryRestResource
public interface EtrackerCrudRepository extends CrudRepository<Expense, Long> {

}

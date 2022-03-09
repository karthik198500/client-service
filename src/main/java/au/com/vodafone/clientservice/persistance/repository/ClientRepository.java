package au.com.vodafone.clientservice.persistance.repository;

import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity,Long> {
}

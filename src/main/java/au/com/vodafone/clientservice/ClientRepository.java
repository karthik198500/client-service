package au.com.vodafone.clientservice;

import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity,Long> {
}

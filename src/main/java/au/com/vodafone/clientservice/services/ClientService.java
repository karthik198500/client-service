package au.com.vodafone.clientservice.services;

import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import au.com.vodafone.clientservice.persistance.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

     ClientRepository clientRepository;

    //Constructor Injection is better
    public ClientService(ClientRepository ClientRepository) {
        this.clientRepository = ClientRepository;
    }



    public ClientEntity insertClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity updateClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity getClientById(String clientId) {
        return clientRepository.findById(Long.valueOf(clientId)).get();
    }

    public void deleteClient(String clientId) {
        clientRepository.deleteById(Long.valueOf(clientId));
    }


    public Optional<ClientEntity> findById(String clientId) {
        return clientRepository.findById(Long.valueOf(clientId));
    }
}

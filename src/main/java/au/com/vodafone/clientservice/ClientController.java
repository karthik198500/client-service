package au.com.vodafone.clientservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

@Controller
public class ClientController {

    private static long idCount = 0;

    private HashMap<Long,ClientEntity> cache = new HashMap<>();

    @Autowired
    ClientRepository repo;

    @PostMapping("/addClient")
    public String AddUser(ClientEntity client){
        client.setId(idCount++);
        repo.save(client);
        cache.put(client.getId(),client);
        return "Success";
    }

    @GetMapping("/GetClient/{id}")
    public ClientEntity getClient(Long id){
        if (cache.containsKey(id)){
            return cache.get(id);
        }
        return repo.findById(id).get();
    }

    @GetMapping("/DeleteClient/{id}")
    public String deleteClient(Long id){
        repo.deleteById(id);
        return "Done";
    }

    @PostMapping("/updateClient")
    public String updateClient(ClientEntity client){
        repo.save(client);
        return "OK";
    }
}

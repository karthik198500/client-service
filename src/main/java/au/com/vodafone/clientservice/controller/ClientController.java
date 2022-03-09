package au.com.vodafone.clientservice.controller;

import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import au.com.vodafone.clientservice.services.ClientService;
import au.com.vodafone.clientservice.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Optional;

@RestController()
@Validated
@Api(tags = "Client",
        value = "ClientController",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(Constants.API_CLIENT_SERVICE_VERSION_CLIENTS)
public class ClientController {

    private final ClientService clientService;

    private static long idCount = 0;
    private HashMap<Long, ClientEntity> cache = new HashMap<>();

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @ApiOperation(value = "Add Client", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> AddUser(@RequestBody ClientEntity client){
        client.setId(idCount++);
        clientService.insertClient(client);
        cache.put(client.getId(),client);
        return ResponseEntity.ok("success");
    }

    @ApiOperation(value = "Get Client Information", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping( value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientEntity> getClient(@PathVariable String clientId){
        if (cache.containsKey(clientId)){
            return ResponseEntity.ok(cache.get(clientId));
        }
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }
    @ApiOperation(value = "Add Client", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @DeleteMapping( value = "{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteClient(@PathVariable String clientId){
        clientService.deleteClient(clientId);
        return  ResponseEntity.ok("success");
    }

    @ApiOperation(value = "Add Client", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping(value = "{clientId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateClient(@PathVariable String clientId,@RequestBody ClientEntity client){
        Optional<ClientEntity> optionalClient  = clientService.findById(clientId);
        if (optionalClient.isPresent()) {
            ClientEntity existingClient = optionalClient.get();
            existingClient.setId(client.getId());
            existingClient.setName(client.getName());
            existingClient.setEmail(client.getEmail());
            clientService.updateClient(existingClient);
            return  ResponseEntity.ok("success");
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

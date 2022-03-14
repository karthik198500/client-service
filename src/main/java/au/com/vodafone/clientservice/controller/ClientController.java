package au.com.vodafone.clientservice.controller;

import au.com.vodafone.clientservice.dto.ClientDTOMapper;
import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import au.com.vodafone.clientservice.dto.ClientDTO;
import au.com.vodafone.clientservice.services.ClientService;
import au.com.vodafone.clientservice.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private ClientDTOMapper clientDTOMapper;


    private HashMap<Long, ClientDTO> cache = new HashMap<>();

    public ClientController(ClientService clientService, ClientDTOMapper clientDTOMapper) {
        this.clientService = clientService;
        this.clientDTOMapper = clientDTOMapper;
    }


    @ApiOperation(value = "Add Client", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> AddUser(@RequestBody ClientDTO client){
        ClientEntity clientEntity = clientDTOMapper.convertClientDTOToClientEntity(client);
        clientEntity =  clientService.insertClient(clientEntity);
        ClientDTO response = clientDTOMapper.convertClientEntityToClientDTO(clientEntity);
        cache.put(client.getId(),response);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Get Client Information", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping( value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getClient(@PathVariable String clientId){
        if (cache.containsKey(clientId)){
            return ResponseEntity.ok(cache.get(clientId));
        }
        return ResponseEntity.ok(clientDTOMapper.convertClientEntityToClientDTO(clientService.getClientById(clientId)));
    }
    @ApiOperation(value = "Delete Client", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @DeleteMapping( value = "{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteClient(@PathVariable String clientId){
        clientService.deleteClient(clientId);
        return  ResponseEntity.ok("Deleted successfully.");
    }

    @ApiOperation(value = "Update Client", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | OK."),
            @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping(value = "{clientId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> updateClient(@PathVariable String clientId,@RequestBody ClientDTO client){
        ClientEntity clientEntity = clientDTOMapper.convertClientDTOToClientEntity(client);

        Optional<ClientEntity> optionalClient  = clientService.findById(clientId);
        if (optionalClient.isPresent()) {
            ClientEntity existingClient = optionalClient.get();
            existingClient.setId(clientEntity.getId());
            existingClient.setName(clientEntity.getName());
            existingClient.setEmail(clientEntity.getEmail());
            ClientEntity updatedValue = clientService.updateClient(existingClient);
            ClientDTO response = clientDTOMapper.convertClientEntityToClientDTO(updatedValue);
            cache.put(client.getId(),response);
            return  ResponseEntity.ok(response);
        } else {
            // Return REST exceptions to keep things simple.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

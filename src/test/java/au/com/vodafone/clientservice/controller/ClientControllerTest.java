package au.com.vodafone.clientservice.controller;

import au.com.vodafone.clientservice.dto.ClientDTO;
import au.com.vodafone.clientservice.dto.ClientDTOMapper;
import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import au.com.vodafone.clientservice.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;

    @MockBean
    private ClientDTOMapper clientDTOMapper;


    @Mock
    private ClientEntity clientEntity;

    @Mock
    private ClientDTO clientDTO;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        clientEntity = new ClientEntity();
        clientEntity.setId(1);
        clientEntity.setName("TestUser");
        clientEntity.setEmail("testuser@gmail.com");

        clientDTO = new ClientDTO();
        clientDTO.setId(1);
        clientDTO.setName("TestUser");
        clientDTO.setEmail("testuser@gmail.com");
    }

    @Test
    @WithMockUser(username="admin", password = "password")
    void addUserSuccess() {

        ClientDTO newClientDTO = new ClientDTO();
        newClientDTO.setName("TestUser");
        newClientDTO.setEmail("testuser@gmail.com");

        when(service.insertClient(Mockito.any())).thenReturn(clientEntity);
        when(clientDTOMapper.convertClientDTOToClientEntity(Mockito.any())).thenReturn(clientEntity);
        when(clientDTOMapper.convertClientEntityToClientDTO(Mockito.any())).thenReturn(clientDTO);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/client-service/v1/clients")
                            .content(asJsonString(newClientDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isCreated())
                            .andExpect(content().json("{\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"name\": \"TestUser\",\n" +
                                    "    \"email\": \"testuser@gmail.com\"\n" +
                                    "}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @WithMockUser(username="admin", password = "password")
    void updateClientSuccess() {

        ClientDTO updatedClientDTO = new ClientDTO();
        updatedClientDTO.setName("TestUser1");
        updatedClientDTO.setEmail("testuser1@gmail.com");

        ClientEntity updatedClientEntity = new ClientEntity();
        updatedClientEntity.setId(1);
        updatedClientEntity.setName("TestUser1");
        updatedClientEntity.setEmail("testuser1@gmail.com");

        ClientDTO finalDTO = new ClientDTO();
        finalDTO.setName("TestUser1");
        finalDTO.setEmail("testuser1@gmail.com");
        finalDTO.setId(1);

        when(service.updateClient(Mockito.any())).thenReturn(updatedClientEntity);
        when(service.findById(Mockito.any())).thenReturn(Optional.of(clientEntity));
        when(clientDTOMapper.convertClientDTOToClientEntity(Mockito.any())).thenReturn(updatedClientEntity);
        when(clientDTOMapper.convertClientEntityToClientDTO(Mockito.any())).thenReturn(finalDTO);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/client-service/v1/clients/1")
                            .content(asJsonString(updatedClientDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"TestUser1\",\n" +
                            "    \"email\": \"testuser1@gmail.com\"\n" +
                            "}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username="admin", password = "password")
    void getClient() {
        when(service.findById(Mockito.any())).thenReturn(Optional.of(clientEntity));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/client-service/v1/clients/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"TestUser\",\n" +
                            "    \"email\": \"testuser@gmail.com\"\n" +
                            "}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username="admin", password = "password")
    void deleteClient() {
        Mockito.doNothing().when(service).deleteClient(Mockito.any());
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/client-service/v1/clients/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("Deleted successfully."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username="admin", password = "password")
    void getClientValidationErrorMethodNotAllowed() {
        when(service.findById(Mockito.any())).thenReturn(Optional.of(clientEntity));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/client-service/v1/clients/"))
                    .andDo(print())
                    .andExpect(status().isMethodNotAllowed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username="admin", password = "password")
    void addUserValidationErrorClientNameNotValid() {

        ClientDTO newClientDTO = new ClientDTO();
        newClientDTO.setName("");
        newClientDTO.setEmail("testusergmail.com");

        ClientEntity newClientEntity = new ClientEntity();
        newClientEntity.setId(1);
        newClientEntity.setName("");
        newClientEntity.setEmail("testusergmail.com");

        when(service.insertClient(Mockito.any())).thenReturn(newClientEntity);
        when(clientDTOMapper.convertClientDTOToClientEntity(Mockito.any())).thenReturn(clientEntity);
        when(clientDTOMapper.convertClientEntityToClientDTO(Mockito.any())).thenReturn(clientDTO);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/client-service/v1/clients")
                            .content(asJsonString(newClientDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username="admin", password = "password")
    void addUserValidationErrorEmailNotValid() {

        ClientDTO newClientDTO = new ClientDTO();
        newClientDTO.setName("Client");
        newClientDTO.setEmail("testusergmail.com");

        ClientEntity newClientEntity = new ClientEntity();
        newClientEntity.setId(1);
        newClientEntity.setName("Client");
        newClientEntity.setEmail("testusergmail.com");

        when(service.insertClient(Mockito.any())).thenReturn(newClientEntity);
        when(clientDTOMapper.convertClientDTOToClientEntity(Mockito.any())).thenReturn(clientEntity);
        when(clientDTOMapper.convertClientEntityToClientDTO(Mockito.any())).thenReturn(clientDTO);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/client-service/v1/clients")
                            .content(asJsonString(newClientDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
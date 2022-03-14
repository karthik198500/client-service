package au.com.vodafone.clientservice.dto;

import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface ClientDTOMapper {

   @Mappings({
            @Mapping(source = "id", target = "id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(source = "name",  target = "name"),
            @Mapping(source = "email", target = "email")
    })
    ClientDTO convertClientEntityToClientDTO(ClientEntity source);
    ClientEntity convertClientDTOToClientEntity(ClientDTO destination);
}

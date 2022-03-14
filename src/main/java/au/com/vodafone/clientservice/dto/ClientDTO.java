package au.com.vodafone.clientservice.dto;

import au.com.vodafone.clientservice.persistance.entities.ClientEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Ideally there is no need for this object, introduced just to make sure if there is any data manipulation that needs
 * to be done incoming object you can do it and send it back as entity.
 *
 * We can use Validators from apache common for email validation or use simple regex.
* */
@Validated
public class ClientDTO {

    @NotNull(message = "Id parameter name cannot be NULL.")
    @NotBlank(message = "Id Input parameter name cannot be blank.")
    @Size(min = 1, max = 70)
    private long id;

    @NotNull (message = "Name parameter name cannot be NULL.")
    @NotBlank (message = "Name parameter name cannot be blank.")
    @Size(min = 1, max = 100)
    private String name;

    @NotNull (message = "Email Input parameter name cannot be NULL.")
    @NotBlank (message = "Email Input parameter name cannot be blank.")
    @Pattern(regexp="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$",
            message = "It allows numeric values from 0 to 9\n" +
                    "Both uppercase and lowercase letters from a to z are allowed\n" +
                    "Allowed are underscore “_”, hyphen “-” and dot “.”\n" +
                    "Dot isn't allowed at the start and end of the local-part\n" +
                    "Consecutive dots aren't allowed\n" +
                    "For the local part, a maximum of 64 characters are allowed")
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

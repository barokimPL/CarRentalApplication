package pl.sda.carrental.model.dataTransfer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.sda.carrental.model.entity.userEntities.Role;
import pl.sda.carrental.model.entity.userEntities.RoleValue;

@Getter
@Setter
@Component
public class UserRegistrationDTO {

    @Size(min = 6, max = 30, message = "Username should be between 6 and 30 characters")
    @NotBlank(message = "First name cannot be blank")
    private String username;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    private String lastName;

    @Email(message = "Email should be a valid email.")
    private String email;

    //TODO: That's cool
    @RoleValue
    private Role role;

    @Size(min = 6, max = 256, message = "Password should be between 6 and 256 characters")
    private String password;

    @Size(min = 6, max = 256, message = "Password should be between 6 and 256 characters")
    private String repeatPassword;

}

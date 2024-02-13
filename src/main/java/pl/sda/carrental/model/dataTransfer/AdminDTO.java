package pl.sda.carrental.model.dataTransfer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.sda.carrental.model.entity.userEntities.Role;

@Getter
@Setter
@Builder
public class AdminDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private boolean isActive;
    private Role role;
}

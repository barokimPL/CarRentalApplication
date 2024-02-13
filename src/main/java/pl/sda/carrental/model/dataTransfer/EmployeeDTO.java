package pl.sda.carrental.model.dataTransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.entity.userEntities.Role;

@Data
@Builder
public class EmployeeDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private boolean isActive;
    private Role role;
    private DivisionDTO divisionDTO;
    private Employee.Position position;


    @AllArgsConstructor
    @Data
    public static class DivisionDTO {
        private Long division_id;
        private String divisionString;
    }
}

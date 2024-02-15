package pl.sda.carrental.util;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.sda.carrental.configuration.configurationProperties.AdminAccount;
import pl.sda.carrental.model.entity.userEntities.Administrator;
import pl.sda.carrental.model.entity.userEntities.Role;
import pl.sda.carrental.model.entity.userEntities.User;
import pl.sda.carrental.model.repository.userRepositories.RoleRepository;
import pl.sda.carrental.model.repository.userRepositories.UserRepository;
import pl.sda.carrental.security.PrincipalRole;

@Profile("prod")
@Component
public class DbInitProd {
    private final AdminAccount adminAccount;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository<Administrator> userRepository;

    public DbInitProd(AdminAccount adminAccount, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository<Administrator> userRepository) {
        this.adminAccount = adminAccount;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Role adminRole = Role.builder().roleName(PrincipalRole.ADMIN.name()).build();
        Role employeeRole = Role.builder().roleName(PrincipalRole.EMPLOYEE.name()).build();
        Role customerRole = Role.builder().roleName(PrincipalRole.CUSTOMER.name()).build();

        roleRepository.save(adminRole);
        roleRepository.save(employeeRole);
        roleRepository.save(customerRole);

        Administrator dbTestAdmin = Administrator.builder()
                .username(adminAccount.getUsername())
                .name(adminAccount.getName())
                .email(adminAccount.getEmail())
                .role(adminRole)
                .password(passwordEncoder.encode(adminAccount.getPassword()))
//                .role(roleRepository.findByRoleName(PrincipalRole.valueOf(adminAccount.getRole()).name()).get())
                .role(roleRepository.findByRoleName(adminAccount.getRole()).get())
                .build();

        userRepository.save(dbTestAdmin);
    }
}

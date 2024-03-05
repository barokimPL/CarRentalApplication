package pl.sda.carrental.configuration.configurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "users.admin")
@Component
public class AdminAccount {
    private String username;
    private String password;
    private String email;
    private String name;
    private String role;
}
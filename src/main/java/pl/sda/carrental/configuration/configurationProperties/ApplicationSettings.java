package pl.sda.carrental.configuration.configurationProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "dev-settings")
@Component
public class ApplicationSettings {
    private int maxFakeUsers;
}

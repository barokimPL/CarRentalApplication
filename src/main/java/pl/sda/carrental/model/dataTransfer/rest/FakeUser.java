package pl.sda.carrental.model.dataTransfer.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import pl.sda.carrental.model.entity.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FakeUser {
    String firstName;
    String lastName;
    String email;
    String password;
    String username;
    Address address;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private class Address {
        String address;
        String city;
        String state;
    }
}

package pl.sda.carrental.model.dataTransfer.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FakeUserResponse {
    private List<FakeUser> users;
}

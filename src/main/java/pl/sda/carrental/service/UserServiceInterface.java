package pl.sda.carrental.service;

import org.springframework.stereotype.Component;
import pl.sda.carrental.model.entity.userEntities.User;

import java.util.List;

@Component
public interface UserServiceInterface {

    void toggleIsActive(long userId);
    List<User> getAllUsers();
    void saveUser(User editedUser);
}

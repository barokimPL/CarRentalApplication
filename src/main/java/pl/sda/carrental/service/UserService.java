package pl.sda.carrental.service;

import org.springframework.stereotype.Service;
import pl.sda.carrental.model.entity.userEntities.User;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.model.repository.userRepositories.UserRepository;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User editedUser) {
        userRepository.save(editedUser);
    }

    @Override
    public void toggleIsActive(long userId) {
        User user = userRepository.findById(userId).get();
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

}

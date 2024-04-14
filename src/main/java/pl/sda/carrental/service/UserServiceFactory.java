package pl.sda.carrental.service;

import org.springframework.stereotype.Component;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.entity.userEntities.User;
import pl.sda.carrental.model.repository.userRepositories.UserRepository;

@Component
public class UserServiceFactory {
    private final UserService userService;
    private final EmployeeService employeeService;
    private final UserRepository userRepository;

    public UserServiceFactory(UserService userService, EmployeeService employeeService, UserRepository userRepository) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.userRepository = userRepository;
    }

    public UserServiceInterface getUserService(long userId) {
        User user = userRepository.findById(userId).get();
        return getUserService(user);
    }
    public UserServiceInterface getUserService() {
        return userService;
    }

    public UserServiceInterface getUserService(User user) {
        if (user instanceof Employee) {
            return employeeService;
        } else {
            return userService;
        }
    }
}

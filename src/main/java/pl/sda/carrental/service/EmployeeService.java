package pl.sda.carrental.service;

import org.springframework.stereotype.Service;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.entity.userEntities.User;
import pl.sda.carrental.model.repository.userRepositories.AdministratorRepository;
import pl.sda.carrental.model.repository.userRepositories.CustomerRepository;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.model.repository.userRepositories.UserRepository;

@Service
public class EmployeeService extends UserService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    public EmployeeService(UserRepository<User> userRepository, AdministratorRepository adminRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository1, AdministratorRepository administratorRepository, UserRepository userRepository1, DivisionService divisionService, EmployeeRepository employeeRepository2, UserRepository userRepository2) {
        super(userRepository, adminRepository, employeeRepository, customerRepository, employeeRepository1, administratorRepository, userRepository1, divisionService);
        this.employeeRepository = employeeRepository2;
        this.userRepository = userRepository2;
    }

    public boolean canBecomeManager(Employee employee) {
        return !employeeRepository.isManger(employee.getId()) && employee.isActive();
    }
    public boolean canBecomeManager(long id) {
        return canBecomeManager(employeeRepository.findById(id).get());
    }
}

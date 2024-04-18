package pl.sda.carrental.service;

import org.springframework.stereotype.Service;
import pl.sda.carrental.constructs.division.DivisionService;
import pl.sda.carrental.constructs.division.Division;
import pl.sda.carrental.constructs.division.exceptions.EmployeeIsManager;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.model.repository.userRepositories.UserRepository;

@Service
public class EmployeeService extends UserService {
    private final EmployeeRepository employeeRepository;
    private final DivisionService divisionService;
    public EmployeeService(DivisionService divisionService, EmployeeRepository employeeRepository, UserRepository userRepository) {
        super(userRepository);
        this.divisionService = divisionService;
        this.employeeRepository = employeeRepository;
    }

    public boolean canBecomeManager(Employee employee) {
        return !employeeRepository.isManger(employee.getId()) && employee.isActive();
    }
    public boolean canBecomeManager(long id) {
        return canBecomeManager(employeeRepository.findById(id).get());
    }

    @Override
    public void toggleIsActive(long employeeId){
        Employee employee = employeeRepository.findById(employeeId).get();
        employee.setActive(!employee.isActive());
        employeeRepository.save(employee);
        Division division = employee.getDivision();
        if (division != null) {
            divisionService.removeEmployee(division, employee);
        }
    }
}

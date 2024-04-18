package pl.sda.carrental.constructs.division;

import org.springframework.stereotype.Service;
import pl.sda.carrental.exception.EmployeeIsManager;
import pl.sda.carrental.exception.CannotBecomeManagerException;
import pl.sda.carrental.model.dataTransfer.CreateDivisionDTO;
import pl.sda.carrental.model.entity.Address;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.AddressRepository;
import pl.sda.carrental.model.repository.DivisionRepository;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DivisionService {
    private final DivisionRepository divisionRepository;
    private final EmployeeRepository employeeRepository;
    private final AddressRepository addressRepository;

    public DivisionService(DivisionRepository divisionRepository, EmployeeRepository employeeRepository, AddressRepository addressRepository) {
        this.divisionRepository = divisionRepository;
        this.employeeRepository = employeeRepository;
        this.addressRepository = addressRepository;
    }

    public void addEmployee(Division division, Employee employee) throws EmployeeIsManager {
        if (employeeRepository.isManger(employee.getId())) {
            throw new EmployeeIsManager("This employee cannot be added here, as he is a manager in another division.");
        }
        division.addEmployee(employee);
        divisionRepository.save(division);
    }

    public void addEmployees(Division division, List<Employee> employees) throws EmployeeIsManager {
        List<String> employeesNotAdded = new ArrayList<>();
        for (Employee e : employees) {
            try {
                addEmployee(division, e);
            } catch (EmployeeIsManager ex) {
                employeesNotAdded.add(e.getName());
            }
        }
        if (!employeesNotAdded.isEmpty()) {
            String message = "Some employees couldn't be added, as they are managers somewhere: " + employeesNotAdded.stream().reduce("", (notAdded, employee) -> notAdded + ", " + employee);
            throw new EmployeeIsManager(message);
        }
    }

    public void removeEmployee(Division division, Employee employee) throws EmployeeIsManager{
        if (employeeRepository.isManger(employee.getId())) {
            throw new EmployeeIsManager("This employee cannot be deleted, as he is a manager.");
        }
        division.getEmployees().remove(employee);
        employee.setDivision(null);
        divisionRepository.save(division);
    }

    public void createDivision(CreateDivisionDTO newDivision, EmployeeService employeeService) throws CannotBecomeManagerException {
        Address address = Address.builder()
                .street(newDivision.getAddress())
                .city(newDivision.getCity())
                .state(newDivision.getState()).build();
        Employee employee = employeeRepository.findById(newDivision.getManager().getId()).get();

        // TODO This could actually just be a jakarta validation
        if (!employeeService.canBecomeManager(employee)) {
            throw new CannotBecomeManagerException("Chosen employee cannot become a manager.");
        }
        addressRepository.save(address);

        Division division = Division.builder()
                .address(address)
                .manager(employee)
                .build();

        divisionRepository.save(division);
        //TODO why are we first adding a manager, and then adding him as en employee?
        //Probably something to do with relation between entities, but still, to be fixed.
        division.addEmployee(employee);
        divisionRepository.save(division);
    }


}

package pl.sda.carrental.service;

import org.springframework.stereotype.Service;
import pl.sda.carrental.exception.CannotBecomeManagerException;
import pl.sda.carrental.model.dataTransfer.CreateDivisionDTO;
import pl.sda.carrental.model.entity.Address;
import pl.sda.carrental.model.entity.Division;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.AddressRepository;
import pl.sda.carrental.model.repository.DivisionRepository;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;

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

    public void addEmployees(Division division, Employee employee) {
        division.addEmployee(employee);
        divisionRepository.save(division);
    }

    public void addEmployees(Division division, List<Employee> employees) {
        employees.forEach(e -> addEmployees(division, e));
    }

    // TODO: exception on trying to remove manager
    public void removeEmployee(Division division, Employee employee) {
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

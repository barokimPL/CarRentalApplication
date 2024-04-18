package pl.sda.carrental.constructs.division;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.carrental.model.entity.Address;
import pl.sda.carrental.model.entity.Car;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Divisions")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long division_id;

    @OneToOne
    private Address address;
    @ToString.Exclude
    // fetch type eager oznacza, że od razu pobierze wszystkich pracowników przy pobieraniu dywizji
    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Employee> employees = new ArrayList<>();
    @OneToMany(mappedBy = "division")
    @ToString.Exclude
    private List<Car> cars = new ArrayList<>();
    @OneToOne
    @ToString.Exclude
    private Employee manager;


    void addEmployee(Employee employee) {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.employees.add(employee);
        employee.setDivision(this);
    }

    public void addCar(Car car) {
        if (this.cars == null)
            this.cars = new ArrayList<>();

        this.cars.add(car);
        car.setDivision(this);
    }

    public static class DivisionBuilder {
        public DivisionBuilder manager(Employee employee) {
            this.manager = employee;
            return this;
        }
    }
}

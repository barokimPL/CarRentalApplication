package pl.sda.carrental.model.repository.userRepositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.sda.carrental.model.entity.Division;
import pl.sda.carrental.model.entity.userEntities.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Override
    List<Employee> findAll();

    List<Employee> findAllByIsActiveIsTrue();

    @Query("SELECT e FROM Employee e where e.isActive = True and e.id not in (select manager.id from Division)")
    List<Employee> findAllActiveNonManagers();

    @Query("SELECT e FROM Employee e where e.isActive = True and e.division.division_id <> :divisionId")
    List<Employee> findAllActiveNonManagersNotInDivision(Long divisionId);

    @Query("SELECT case when count(e) > 0 then true else false end FROM Employee e where e.id = :employee_id and e.id in (select manager.id from Division)")
    boolean isManger(@Param("employee_id") Long id);

    boolean existsById(@NotNull Long id);
    Optional<Employee> findByUsername(String username);
}

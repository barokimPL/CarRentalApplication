package pl.sda.carrental.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.carrental.constructs.division.Division;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    @Override
    List<Division> findAll();
}

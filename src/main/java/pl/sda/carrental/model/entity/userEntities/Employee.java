package pl.sda.carrental.model.entity.userEntities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.sda.carrental.model.entity.Division;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class Employee extends User {

    @Enumerated(EnumType.STRING)
    private Position position;
    
    @ManyToOne
    private Division division;

//    @OneToMany(mappedBy = "employee")
//    private List<CarRental> carRental = new ArrayList<>();

    // rev: wewnętrzne enumy zawsze są statyczne
    public enum Position {
        EMPLOYEE,
        MANAGER
    }
}
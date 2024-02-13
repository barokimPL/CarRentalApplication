package pl.sda.carrental.model.entity.userEntities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.sda.carrental.model.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@Entity
@NoArgsConstructor
public class Customer extends User {
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Reservation> reservations = new ArrayList<>();

    private Long reservation_id;
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
        this.reservation_id = reservation.getId();
    }
    public List<Reservation> getCustomerReservations(){
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        return this.reservations;
    }
    public Long getReservationIdFromUser(){
        return this.reservation_id;
    }
}

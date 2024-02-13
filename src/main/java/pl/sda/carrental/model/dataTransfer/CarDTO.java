package pl.sda.carrental.model.dataTransfer;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import pl.sda.carrental.model.entity.Car;
import pl.sda.carrental.model.entity.Division;
import pl.sda.carrental.model.entity.Reservation;

import java.math.BigDecimal;
import java.time.Year;

@Data
public class CarDTO {

    private Long id;

    private Division division;

    private Reservation reservation;

    private String brand;

    private String model;

    private String bodyType;

    private Year productionYear;

    private String color;

    private int mileage;

    @Enumerated(EnumType.STRING)
    private Car.RentStatus status;

    private BigDecimal costPerDay;
}

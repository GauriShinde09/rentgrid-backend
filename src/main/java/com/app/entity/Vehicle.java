
package com.app.entity;

import java.math.BigDecimal;

import com.app.enums.VehicleStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false, length = 30)
    private String vehicleType;

    @Column(nullable = false, unique = true, length = 20)
    private String registrationNumber;

    @Column(nullable = false, length = 30)
    private String fuelType;

    @Column(nullable = false, length = 30)
    private String transmission;

    private Integer seatingCapacity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    private Integer includedKmPerDay;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal extraKmRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleStatus status;

    @Column(length = 1000)
    private String description;
}


package com.app.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.entity.Vehicle;
import com.app.enums.VehicleStatus;
import com.app.service.VehicleService;

@RestController
@RequestMapping("/rentgrid/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Add a vehicle
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }

    // Get all vehicles
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // Get vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // Update a vehicle
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }

    // Delete a vehicle
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vehicle>> getVehiclesByStatus(
            @PathVariable VehicleStatus status) {

        return ResponseEntity.ok(
                vehicleService.getVehiclesByStatus(status));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Vehicle>> searchVehiclesByBrand(
            @RequestParam String brand) {

        return ResponseEntity.ok(
                vehicleService.searchVehiclesByBrand(brand));
    }
    @GetMapping("/search/type")
    public ResponseEntity<List<Vehicle>> searchVehiclesByType(
            @RequestParam String vehicleType) {

        return ResponseEntity.ok(
                vehicleService.searchVehiclesByType(vehicleType));
    }
    
    @GetMapping("/search/price")
    public ResponseEntity<List<Vehicle>> searchVehiclesByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        return ResponseEntity.ok(
                vehicleService.searchVehiclesByPrice(
                        minPrice, maxPrice));
    }
    
    @GetMapping("/search/seating")
    public ResponseEntity<List<Vehicle>> searchVehiclesBySeatingCapacity(
            @RequestParam Integer seatingCapacity) {

        return ResponseEntity.ok(
                vehicleService.searchVehiclesBySeatingCapacity(
                        seatingCapacity));
    }
    
    @GetMapping("/sort/price/asc")
    public ResponseEntity<List<Vehicle>> sortVehiclesByPriceAsc() {
        return ResponseEntity.ok(
                vehicleService.getVehiclesSortedByPriceAsc());
    }

    @GetMapping("/sort/price/desc")
    public ResponseEntity<List<Vehicle>> sortVehiclesByPriceDesc() {
        return ResponseEntity.ok(
                vehicleService.getVehiclesSortedByPriceDesc());
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Vehicle>> getVehiclesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                vehicleService.getAllVehicles(page, size));
    }
    
 // Check vehicle availability for selected dates
    @GetMapping("/availability")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate pickupDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate returnDate) {

        return ResponseEntity.ok(
                vehicleService.getAvailableVehicles(
                        pickupDate, returnDate));
    }
}

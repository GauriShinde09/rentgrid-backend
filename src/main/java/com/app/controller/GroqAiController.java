
package com.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.DamageAssessmentDto;
import com.app.dto.VehicleRequirementDto;
import com.app.entity.Vehicle;
import com.app.service.GroqAiService;
import com.app.service.VehicleService;

@RestController
@RequestMapping("/rentgrid/ai")
public class GroqAiController {

    private final GroqAiService groqAiService;
    private final VehicleService vehicleService;
    public GroqAiController(GroqAiService groqAiService, VehicleService vehicleService) {
        this.groqAiService = groqAiService;
		this.vehicleService = vehicleService;
    }

    @PostMapping("/test")
    public Map<String, String> testAI(
            @RequestBody Map<String, String> request) {

        String message = request.get("message");

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty");
        }

        String answer = groqAiService.askAI(message);

        return Map.of("answer", answer);
    }
    @PostMapping("/availability")
    public ResponseEntity<List<Vehicle>> findVehicles(
            @RequestBody Map<String, String> request) {

        String message = request.get("message");

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Please describe your vehicle requirements."
            );
        }

        VehicleRequirementDto requirements =
                groqAiService.extractRequirements(message);

        if (requirements.getPickupDate() == null
                || requirements.getReturnDate() == null) {
            throw new IllegalArgumentException(
                    "Please provide pickup and return dates."
            );
        }

        List<Vehicle> vehicles = vehicleService.getAvailableVehicles(
                requirements.getPickupDate(),
                requirements.getReturnDate()
        );

        if (requirements.getVehicleType() != null) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getVehicleType()
                            .equalsIgnoreCase(requirements.getVehicleType()))
                    .toList();
        }

        if (requirements.getSeatingCapacity() != null) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getSeatingCapacity()
                            >= requirements.getSeatingCapacity())
                    .toList();
        }

        if (requirements.getMaxPricePerDay() != null) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getPricePerDay()
                            .compareTo(requirements.getMaxPricePerDay()) <= 0)
                    .toList();
        }

        return ResponseEntity.ok(vehicles);
    }
    
    @PostMapping("/assess-damage")
    public ResponseEntity<DamageAssessmentDto> assessDamage(
            @RequestBody Map<String, String> request) {

        String description = request.get("description");

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Please provide a damage description");
        }

        DamageAssessmentDto result =
                groqAiService.assessDamage(description);

        return ResponseEntity.ok(result);
    }
}



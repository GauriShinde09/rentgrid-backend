
package com.app.service.impl;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.AvailabilityNotificationRequestDto;
import com.app.entity.AvailabilityNotification;
import com.app.entity.User;
import com.app.entity.Vehicle;
import com.app.enums.VehicleStatus;
import com.app.repository.AvailabilityNotificationRepository;
import com.app.repository.UserRepository;
import com.app.repository.VehicleRepository;
import com.app.service.AvailabilityNotificationService;

@Service
public class AvailabilityNotificationServiceImpl
        implements AvailabilityNotificationService {

    private final AvailabilityNotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public AvailabilityNotificationServiceImpl(
            AvailabilityNotificationRepository notificationRepository,
            UserRepository userRepository,
            VehicleRepository vehicleRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public String createRequest(
            AvailabilityNotificationRequestDto request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        LocalDate pickupDate = request.getPickupDate();
        LocalDate returnDate = request.getReturnDate();

        if (pickupDate == null || returnDate == null
                || pickupDate.isBefore(LocalDate.now())
                || !returnDate.isAfter(pickupDate)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please select valid pickup and return dates");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));

        if (vehicle.getStatus() == VehicleStatus.UNDER_MAINTENANCE
                || vehicle.getStatus() == VehicleStatus.OUT_OF_SERVICE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Notifications cannot be requested for this vehicle");
        }

        boolean alreadyRequested =
                notificationRepository
                        .existsByUserIdAndVehicleIdAndPickupDateAndReturnDateAndStatus(
                                user.getId(),
                                vehicle.getId(),
                                pickupDate,
                                returnDate,
                                "WAITING");

        if (alreadyRequested) {
            return "You have already requested a notification for these dates.";
        }

        AvailabilityNotification notification =
                new AvailabilityNotification();

        notification.setUser(user);
        notification.setVehicle(vehicle);
        notification.setPickupDate(pickupDate);
        notification.setReturnDate(returnDate);
        notification.setStatus("WAITING");

        notificationRepository.save(notification);

        return "Your notification request has been saved successfully.";
    }
}

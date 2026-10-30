
package com.app.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entity.Booking;
import com.app.entity.Vehicle;
import com.app.enums.BookingStatus;
import com.app.enums.VehicleStatus;
import com.app.exception.VehicleNotFoundException;
import com.app.repository.BookingRepository;
import com.app.repository.VehicleRepository;
import com.app.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    public VehicleServiceImpl(VehicleRepository vehicleRepository, BookingRepository bookingRepository) {
        this.vehicleRepository = vehicleRepository;
		this.bookingRepository = bookingRepository;
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new VehicleNotFoundException(
                                "Vehicle not found with id: " + id));
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found with id: " + id));

        existingVehicle.setName(vehicle.getName());
        existingVehicle.setBrand(vehicle.getBrand());
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setVehicleType(vehicle.getVehicleType());
        existingVehicle.setRegistrationNumber(vehicle.getRegistrationNumber());
        existingVehicle.setFuelType(vehicle.getFuelType());
        existingVehicle.setTransmission(vehicle.getTransmission());
        existingVehicle.setSeatingCapacity(vehicle.getSeatingCapacity());
        existingVehicle.setPricePerDay(vehicle.getPricePerDay());
        existingVehicle.setIncludedKmPerDay(vehicle.getIncludedKmPerDay());
        existingVehicle.setExtraKmRate(vehicle.getExtraKmRate());
        existingVehicle.setStatus(vehicle.getStatus());
        existingVehicle.setDescription(vehicle.getDescription());

        return vehicleRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found with id: " + id));

        vehicleRepository.delete(vehicle);
    }

	@Override
	public List<Vehicle> getVehiclesByStatus(VehicleStatus status) {
		// TODO Auto-generated method stub
		 return vehicleRepository.findByStatus(status);
	}
	@Override
	public List<Vehicle> searchVehiclesByBrand(String brand) {
	    return vehicleRepository.findByBrandIgnoreCase(brand);
	}
	
	@Override
	public List<Vehicle> searchVehiclesByType(String vehicleType) {
	    return vehicleRepository.findByVehicleTypeIgnoreCase(vehicleType);
	}

	@Override
	public List<Vehicle> searchVehiclesByPrice(
	        BigDecimal minPrice,
	        BigDecimal maxPrice) {

	    if (minPrice.compareTo(BigDecimal.ZERO) < 0
	            || maxPrice.compareTo(minPrice) < 0) {
	        throw new IllegalArgumentException(
	                "Invalid price range");
	    }

	    return vehicleRepository.findByPricePerDayBetween(
	            minPrice, maxPrice);
	}

	@Override
	public List<Vehicle> searchVehiclesBySeatingCapacity(
	        Integer seatingCapacity) {

	    if (seatingCapacity <= 0) {
	        throw new IllegalArgumentException(
	                "Seating capacity must be greater than zero");
	    }

	    return vehicleRepository.findBySeatingCapacity(seatingCapacity);
	}
	@Override
	public List<Vehicle> getVehiclesSortedByPriceAsc() {
	    return vehicleRepository.findAll(
	            Sort.by(Sort.Direction.ASC, "pricePerDay"));
	}

	@Override
	public List<Vehicle> getVehiclesSortedByPriceDesc() {
	    return vehicleRepository.findAll(
	            Sort.by(Sort.Direction.DESC, "pricePerDay"));
	}
	@Override
	public Page<Vehicle> getAllVehicles(int page, int size) {

	    if (page < 0 || size <= 0 || size > 100) {
	        throw new IllegalArgumentException(
	                "Invalid page or size. Size must be between 1 and 100");
	    }

	    return vehicleRepository.findAll(
	            PageRequest.of(page, size));
	}
	
	@Override
	public List<Vehicle> getAvailableVehicles(
	        LocalDate pickupDate,
	        LocalDate returnDate) {

	    if (pickupDate == null || returnDate == null
	            || pickupDate.isBefore(LocalDate.now())
	            || !returnDate.isAfter(pickupDate)) {

	        throw new IllegalArgumentException(
	                "Select valid pickup and return dates");
	    }

	    List<Vehicle> availableVehicles =
	            vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);

	    List<BookingStatus> blockingStatuses = List.of(
	            BookingStatus.PENDING,
	            BookingStatus.CONFIRMED,
	            BookingStatus.ACTIVE
	    );

	    List<Vehicle> result = new ArrayList<>();

	    for (Vehicle vehicle : availableVehicles) {

	        List<Booking> conflicts = bookingRepository
	                .findByVehicleIdAndStatusInAndPickupDateLessThanEqualAndReturnDateGreaterThanEqual(
	                        vehicle.getId(),
	                        blockingStatuses,
	                        returnDate,
	                        pickupDate
	                );

	        if (conflicts.isEmpty()) {
	            result.add(vehicle);
	        }
	    }

	    return result;
	}
}

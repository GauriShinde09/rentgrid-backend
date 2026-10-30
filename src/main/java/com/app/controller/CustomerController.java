
package com.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CustomerResponseDto;
import com.app.dto.CustomerUpdateRequestDto;
import com.app.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentgrid/customers")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // Search customers by name
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDto>> searchCustomers(
            @RequestParam String name) {
        return ResponseEntity.ok(
                customerService.searchCustomersByName(name));
    }
    

@GetMapping("/page")
public ResponseEntity<Page<CustomerResponseDto>> getCustomersPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

    return ResponseEntity.ok(
            customerService.getAllCustomers(page, size));
}

@PutMapping("/{id}")
public ResponseEntity<CustomerResponseDto> updateCustomer(
        @PathVariable Long id,
        @Valid @RequestBody CustomerUpdateRequestDto requestDto) {

    return ResponseEntity.ok(
            customerService.updateCustomer(id, requestDto));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

    customerService.deleteCustomer(id);

    return ResponseEntity.noContent().build();
}

}

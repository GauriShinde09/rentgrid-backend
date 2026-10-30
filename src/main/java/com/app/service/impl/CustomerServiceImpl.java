
package com.app.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.CustomerResponseDto;
import com.app.dto.CustomerUpdateRequestDto;
import com.app.entity.User;
import com.app.enums.Role;
import com.app.repository.UserRepository;
import com.app.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;

    public CustomerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return userRepository.findByRole(Role.CUSTOMER)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.CUSTOMER)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found with id: " + id));

        return convertToDto(user);
    }

    @Override
    public List<CustomerResponseDto> searchCustomersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .filter(user -> user.getRole() == Role.CUSTOMER)
                .map(this::convertToDto)
                .toList();
    }

    private CustomerResponseDto convertToDto(User user) {
        CustomerResponseDto dto = new CustomerResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
    @Override
    public Page<CustomerResponseDto> getAllCustomers(int page, int size) {

        if (page < 0 || size <= 0 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid page or size. Size must be between 1 and 100");
        }

        return userRepository.findByRole(
                Role.CUSTOMER,
                PageRequest.of(page, size)
        ).map(this::convertToDto);
    }

@Override
public CustomerResponseDto updateCustomer(
        Long id, CustomerUpdateRequestDto requestDto) {

    User customer = userRepository.findById(id)
            .filter(user -> user.getRole() == Role.CUSTOMER)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found with id: " + id));

    customer.setName(requestDto.getName());
    customer.setPhone(requestDto.getPhone());

    User updatedCustomer = userRepository.save(customer);

    return convertToDto(updatedCustomer);
}
@Override
public void deleteCustomer(Long id) {

    User customer = userRepository.findById(id)
            .filter(user -> user.getRole() == Role.CUSTOMER)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found with id: " + id));

    userRepository.delete(customer);
}
}

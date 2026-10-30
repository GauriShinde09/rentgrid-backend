
package com.app.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.dto.CustomerResponseDto;
import com.app.dto.CustomerUpdateRequestDto;

public interface CustomerService {

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto getCustomerById(Long id);

    List<CustomerResponseDto> searchCustomersByName(String name);

    Page<CustomerResponseDto> getAllCustomers(int page, int size);
    CustomerResponseDto updateCustomer(
            Long id,
            CustomerUpdateRequestDto requestDto);
    void deleteCustomer(Long id);
}

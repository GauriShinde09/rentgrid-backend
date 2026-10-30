
package com.app.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.entity.Bill;
import com.app.repository.BillRepository;
import com.app.service.BillService;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public Bill generateBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Optional<Bill> getBillByBookingId(Long bookingId) {
        return billRepository.findByBookingId(bookingId);
    }
}

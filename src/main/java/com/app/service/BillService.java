
package com.app.service;

import java.util.Optional;
import com.app.entity.Bill;

public interface BillService {
    Bill generateBill(Bill bill);
    Optional<Bill> getBillByBookingId(Long bookingId);
}

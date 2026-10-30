
package com.app.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.RevenueReportDto;
import com.app.repository.InvoiceRepository;
import com.app.service.RevenueReportService;

@Service
@Transactional(readOnly = true)
public class RevenueReportServiceImpl implements RevenueReportService {

    private final InvoiceRepository invoiceRepository;

    public RevenueReportServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public RevenueReportDto getRevenueReport() {

        RevenueReportDto dto = new RevenueReportDto();

        dto.setTotalRevenue(
                invoiceRepository.getTotalRevenue());

        dto.setTotalRentalRevenue(
                invoiceRepository.getTotalRentalRevenue());

        dto.setTotalExtraKmCharges(
                invoiceRepository.getTotalExtraKmCharges());

        dto.setTotalLateReturnCharges(
                invoiceRepository.getTotalLateReturnCharges());

        dto.setTotalDamageCharges(
                invoiceRepository.getTotalDamageCharges());

        return dto;
    }
}

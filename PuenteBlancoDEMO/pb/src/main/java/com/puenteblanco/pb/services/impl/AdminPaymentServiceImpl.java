package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.AdminPaymentResponseDto;
import com.puenteblanco.pb.repository.PagoRepository;
import com.puenteblanco.pb.services.interfaces.AdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PagoRepository pagoRepository;

    @Override
    public List<AdminPaymentResponseDto> obtenerPagos() {
        return pagoRepository.findAllPaymentsForAdmin();
    }
}
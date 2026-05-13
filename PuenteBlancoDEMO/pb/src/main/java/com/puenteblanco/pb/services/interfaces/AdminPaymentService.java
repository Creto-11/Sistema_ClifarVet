package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.AdminPaymentResponseDto;

import java.util.List;

public interface AdminPaymentService {
    List<AdminPaymentResponseDto> obtenerPagos();
}
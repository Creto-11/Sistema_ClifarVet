package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.VetInternSimpleResponseDto;
import java.util.List;

public interface VetInternService {
    List<VetInternSimpleResponseDto> findAllInterns();
}
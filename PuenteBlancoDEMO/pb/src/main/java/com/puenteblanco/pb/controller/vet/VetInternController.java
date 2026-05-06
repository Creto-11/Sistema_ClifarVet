package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.response.VetInternSimpleResponseDto;
import com.puenteblanco.pb.services.interfaces.VetInternService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vet/interns")
public class VetInternController {

    private final VetInternService vetInternService;

    public VetInternController(VetInternService vetInternService) {
        this.vetInternService = vetInternService;
    }

    @GetMapping
    public List<VetInternSimpleResponseDto> getAllInterns() {
        return vetInternService.findAllInterns();
    }
}

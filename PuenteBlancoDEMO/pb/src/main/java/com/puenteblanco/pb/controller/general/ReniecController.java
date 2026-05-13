package com.puenteblanco.pb.controller.general;

import com.puenteblanco.pb.services.interfaces.ReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reniec")
@CrossOrigin(origins = "http://localhost:8082") 
public class ReniecController {

    @Autowired
    private ReniecService reniecService;

    @GetMapping("/dni/{numero}")
    public Map<String, Object> obtenerDatosPorDni(@PathVariable String numero) {
        return reniecService.consultarPorDni(numero);
    }
}

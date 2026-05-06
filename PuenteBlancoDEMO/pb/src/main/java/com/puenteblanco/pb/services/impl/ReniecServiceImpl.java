package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.services.interfaces.ReniecService;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class ReniecServiceImpl implements ReniecService {

    private final String API_URL = "https://api.apis.net.pe/v2/reniec/dni?numero=";
    private final String TOKEN = "apis-token-16341.cXDDQG15OxxWuVSjIeGrdYShM5G64PsQ";

    @Override
    @SuppressWarnings("unchecked") // Silenciamos el aviso del cast de Map
    public Map<String, Object> consultarPorDni(@NonNull String dni) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Especificamos Map.class pero lo manejamos como Map<String, Object>
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL + dni,
                    HttpMethod.GET,
                    entity,
                    Map.class);

            Map<String, Object> body = (Map<String, Object>) response.getBody();
            return body != null ? body : Collections.emptyMap();
            
        } catch (Exception e) {
            // En caso de error de API, devolvemos un mapa vacío para evitar NullPointer
            return Collections.emptyMap();
        }
    }
}
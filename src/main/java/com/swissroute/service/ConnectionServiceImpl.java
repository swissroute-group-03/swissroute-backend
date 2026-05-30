package com.swissroute.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swissroute.dto.response.ConnectionResponseDTO;
import com.swissroute.dto.response.SeccionDTO;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.use_cases.ConnectionUseCase;

@Service
public class ConnectionServiceImpl implements ConnectionUseCase {

    private final WebClient transportWebClient;

    public ConnectionServiceImpl(WebClient transportWebClient) {
        this.transportWebClient = transportWebClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConnectionResponseDTO> buscarConexiones(String from, String to, String date, String time, String transportations, List<String> via) {
        try {
            Map<?, ?> response = transportWebClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/connections")
                                .queryParam("from", from)
                                .queryParam("to", to);
                        if (date != null && !date.isBlank()) uriBuilder.queryParam("date", date);
                        if (time != null && !time.isBlank()) uriBuilder.queryParam("time", time);
                        if (transportations != null && !transportations.isBlank()) uriBuilder.queryParam("transportations", transportations);
                        if (via != null && !via.isEmpty()) {
                            if (via.size() == 1) {
                                uriBuilder.queryParam("via", via.get(0));
                            } else {
                                via.forEach(stop -> uriBuilder.queryParam("via[]", stop));
                            }
                        }
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> connections = (List<Map<String, Object>>) response.get("connections");

            if (connections == null || connections.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No se encontraron conexiones entre " + from + " y " + to);
            }

            return connections.stream()
                    .map(this::toResponseDTO)
                    .toList();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    @SuppressWarnings("unchecked")
    private ConnectionResponseDTO toResponseDTO(Map<String, Object> conn) {
        ConnectionResponseDTO dto = new ConnectionResponseDTO();

        Map<String, Object> from = (Map<String, Object>) conn.get("from");
        Map<String, Object> fromStation = (Map<String, Object>) from.get("station");
        dto.setOrigen((String) fromStation.get("name"));

        Map<String, Object> to = (Map<String, Object>) conn.get("to");
        Map<String, Object> toStation = (Map<String, Object>) to.get("station");
        dto.setDestino((String) toStation.get("name"));

        dto.setDuracion((String) conn.get("duration"));

        List<String> products = (List<String>) conn.get("products");
        dto.setProductos(products != null ? products : List.of());

        List<Map<String, Object>> sections = (List<Map<String, Object>>) conn.get("sections");
        dto.setSecciones(sections != null
                ? sections.stream().map(this::toSeccionDTO).toList()
                : List.of());

        return dto;
    }

    @SuppressWarnings("unchecked")
    private SeccionDTO toSeccionDTO(Map<String, Object> section) {
        SeccionDTO dto = new SeccionDTO();

        Map<String, Object> departure = (Map<String, Object>) section.get("departure");
        Map<String, Object> arrival = (Map<String, Object>) section.get("arrival");

        Map<String, Object> depStation = (Map<String, Object>) departure.get("station");
        Map<String, Object> arrStation = (Map<String, Object>) arrival.get("station");

        dto.setOrigen((String) depStation.get("name"));
        dto.setDestino((String) arrStation.get("name"));
        dto.setSalida((String) departure.get("departure"));
        dto.setLlegada((String) arrival.get("arrival"));

        Map<String, Object> journey = (Map<String, Object>) section.get("journey");
        if (journey != null) {
            String category = (String) journey.get("category");
            String number = (String) journey.get("number");
            String operator = (String) journey.get("operator");
            dto.setTransporte((category != null ? category : "") + " "
                    + (number != null ? number : "") + " "
                    + (operator != null ? operator : ""));
            dto.setTipo("journey");
        } else {
            Map<String, Object> walk = (Map<String, Object>) section.get("walk");
            if (walk != null) {
                dto.setTransporte("Caminando");
                dto.setTipo("walk");
            }
        }

        return dto;
    }
}

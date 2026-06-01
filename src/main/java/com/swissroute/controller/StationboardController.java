package com.swissroute.controller;

import com.swissroute.dto.response.StationboardDTO;
import com.swissroute.service.use_cases.StationboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Tablón de Estaciones", description = "Consulta de salidas programadas en una estación")
public class StationboardController {

    private final StationboardService stationboardService;

    @GetMapping("/tablon")
    @Operation(
        summary = "Consultar tablón de salidas",
        description = "Obtiene las salidas programadas de una estación, con filtro opcional por tipo de transporte"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de salidas programadas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StationboardDTO.class))
        )
    })
    public ResponseEntity<List<StationboardDTO>> getStationboard(
            @Parameter(description = "Nombre de la estación", example = "Lausanne", required = true)
            @RequestParam String station,
            @Parameter(description = "Cantidad máxima de resultados", example = "10")
            @RequestParam(defaultValue = "10") Integer limit,
            @Parameter(description = "Tipo de transporte (train, bus, tram, etc.)", example = "train")
            @RequestParam(required = false) String type) {

        Optional<String> transportType = Optional.ofNullable(type);

        List<StationboardDTO> stationboard = stationboardService.getStationboard(
                station, limit, transportType);

        return ResponseEntity.ok(stationboard);
    }


}
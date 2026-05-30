package com.swissroute.controller;

import com.swissroute.dto.response.StationboardDTO;
import com.swissroute.service.use_cases.StationboardService;
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
public class StationboardController {

    private final StationboardService stationboardService;

    @GetMapping("/tablon")
    public ResponseEntity<List<StationboardDTO>> getStationboard(
            @RequestParam String station,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String type) {

        Optional<String> transportType = Optional.ofNullable(type);

        List<StationboardDTO> stationboard = stationboardService.getStationboard(
                station, limit, transportType);

        return ResponseEntity.ok(stationboard);
    }


}
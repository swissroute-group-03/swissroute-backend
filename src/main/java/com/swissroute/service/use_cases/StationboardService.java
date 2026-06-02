package com.swissroute.service.use_cases;

import com.swissroute.dto.response.StationboardDTO;

import java.util.List;
import java.util.Optional;

public interface StationboardService {

    List<StationboardDTO> getStationboard(
            String station,
            Integer limit,
            Optional<String> transportType);
}

package com.swissroute.service.use_cases;

import java.util.List;

import com.swissroute.dto.response.RoleResponseDTO;

public interface RoleUseCase {
   List<RoleResponseDTO> getRoles(); 
}

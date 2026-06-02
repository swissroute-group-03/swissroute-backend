package com.swissroute.service.use_cases;

import org.springframework.security.core.Authentication;

import com.swissroute.dto.request.AuthRequestDTO;
import com.swissroute.dto.response.AuthResponseDTO;

public interface AuthUseCase {
    AuthResponseDTO login(AuthRequestDTO request);
    Authentication authenticate(String username, String password);
}

package com.swissroute.service.use_cases;

import com.swissroute.dto.request.UserRequestDTO;
import com.swissroute.dto.response.UserResponseDTO;

public interface UserUseCase {
    UserResponseDTO createUser(UserRequestDTO userDTO);
}

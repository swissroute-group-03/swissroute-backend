package com.swissroute.service.use_cases;

import com.swissroute.dto.UserRequestDTO;
import com.swissroute.dto.UserResponseDTO;

public interface UserUseCase {
    UserResponseDTO createUser(UserRequestDTO userDTO);
}

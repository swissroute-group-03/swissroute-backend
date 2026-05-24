package com.swissroute.service;

import com.swissroute.constant.ExceptionMessagesConstants;
import com.swissroute.constant.RoleConstants;
import com.swissroute.exceptionHandler.exceptions.ConflictException;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swissroute.dto.request.UserRequestDTO;
import com.swissroute.dto.response.UserResponseDTO;
import com.swissroute.mapper.UserMapper;
import com.swissroute.model.Role;
import com.swissroute.model.User;
import com.swissroute.repository.RoleRepository;
import com.swissroute.repository.UserRepository;
import com.swissroute.service.use_cases.UserUseCase;

@Service
public class UserServiceImpl implements UserUseCase{
    private UserMapper userMapper;
    
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new ConflictException(ExceptionMessagesConstants.EMAIL_YA_REGISTRADO);
        }

        Role role = roleRepository.findByName(RoleConstants.VISITANTE)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessagesConstants.ROL_NO_ENCONTRADO));
        User user = userMapper.toEntity(userRequestDTO);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        User userGuardado = userRepository.save(user);

        return userMapper.toDto(userGuardado);
    }

}

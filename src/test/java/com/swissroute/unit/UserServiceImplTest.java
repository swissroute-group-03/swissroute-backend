package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.swissroute.constant.ExceptionMessagesConstants;
import com.swissroute.constant.RoleConstants;
import com.swissroute.dto.request.UserRequestDTO;
import com.swissroute.dto.response.UserResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ConflictException;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.mapper.UserMapper;
import com.swissroute.model.Role;
import com.swissroute.model.User;
import com.swissroute.repository.RoleRepository;
import com.swissroute.repository.UserRepository;
import com.swissroute.service.UserServiceImpl;

class UserServiceImplTest {

    private UserMapper userMapper;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userMapper, userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void createUser_ShouldReturnUserResponseDTO_WhenValidRequest() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("new@example.com");
        request.setPassword("password123");

        Role role = new Role();
        role.setName(RoleConstants.VISITANTE);

        User user = new User();
        user.setEmail(request.getEmail());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());

        UserResponseDTO expectedResponse = new UserResponseDTO();
        expectedResponse.setId(1L);
        expectedResponse.setEmail(request.getEmail());

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleConstants.VISITANTE)).thenReturn(Optional.of(role));
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedResponse);

        UserResponseDTO result = userService.createUser(request);

        assertEquals(expectedResponse.getEmail(), result.getEmail());
        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    void createUser_ShouldThrowConflictException_WhenEmailAlreadyExists() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class,
                () -> userService.createUser(request));

        assertEquals(ExceptionMessagesConstants.EMAIL_YA_REGISTRADO, ex.getMessage());
    }

    @Test
    void createUser_ShouldThrowResourceNotFoundException_WhenRoleNotFound() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("new@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleConstants.VISITANTE)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.createUser(request));

        assertEquals(ExceptionMessagesConstants.ROL_NO_ENCONTRADO, ex.getMessage());
    }
}

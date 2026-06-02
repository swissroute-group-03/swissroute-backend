package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.swissroute.constant.ExceptionMessagesConstants;
import com.swissroute.constant.ResponseMessagesConstants;
import com.swissroute.dto.request.AuthRequestDTO;
import com.swissroute.dto.response.AuthResponseDTO;
import com.swissroute.model.User;
import com.swissroute.repository.UserRepository;
import com.swissroute.service.AuthServiceImpl;
import com.swissroute.util.JwtUtils;

class AuthServiceImplTest {

    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsService.class);
        userRepository = mock(UserRepository.class);
        authService = new AuthServiceImpl(passwordEncoder, jwtUtils, userDetailsService, userRepository);
    }

    @Test
    void login_ShouldReturnAuthResponseDTO_WhenCredentialsAreValid() {
        String email = "test@example.com";
        String password = "password123";
        AuthRequestDTO request = new AuthRequestDTO(email, password);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                email, "encodedPassword", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtils.createToken(any(Authentication.class), eq(1L))).thenReturn("mockToken");

        AuthResponseDTO result = authService.login(request);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals("mockToken", result.getJwt());
        assertEquals(ResponseMessagesConstants.LOGIN_EXITOSO, result.getMessage());
        assertTrue(result.getStatus());
    }

    @Test
    void login_ShouldThrowBadCredentialsException_WhenUserDoesNotExistInRepository() {
        String email = "test@example.com";
        AuthRequestDTO request = new AuthRequestDTO(email, "password123");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                email, "encodedPassword", Collections.emptyList());

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void authenticate_ShouldReturnAuthentication_WhenCredentialsAreValid() {
        String email = "test@example.com";
        String password = "password123";

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                email, "encodedPassword", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        Authentication result = authService.authenticate(email, password);

        assertNotNull(result);
        assertEquals(email, result.getPrincipal());
        assertEquals("encodedPassword", result.getCredentials());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void authenticate_ShouldThrowBadCredentialsException_WhenUserNotFoundInDetailsService() {
        when(userDetailsService.loadUserByUsername(any())).thenReturn(null);

        BadCredentialsException ex = assertThrows(BadCredentialsException.class, 
                () -> authService.authenticate("nonexistent@example.com", "pass"));
        
        assertEquals(ExceptionMessagesConstants.CREDENCIALES_INVALIDAS, ex.getMessage());
    }

    @Test
    void authenticate_ShouldThrowBadCredentialsException_WhenPasswordDoesNotMatch() {
        String email = "test@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                email, "encodedPassword", Collections.emptyList());

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        BadCredentialsException ex = assertThrows(BadCredentialsException.class, 
                () -> authService.authenticate(email, "wrongPassword"));
        
        assertEquals(ExceptionMessagesConstants.CREDENCIALES_INVALIDAS, ex.getMessage());
    }
}

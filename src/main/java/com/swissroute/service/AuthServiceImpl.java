package com.swissroute.service;

import com.swissroute.constant.ExceptionMessagesConstants;
import com.swissroute.constant.ResponseMessagesConstants;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swissroute.dto.AuthRequestDTO;
import com.swissroute.dto.AuthResponseDTO;
import com.swissroute.service.use_cases.AuthUseCase;
import com.swissroute.util.JwtUtils;

@Service
public class AuthServiceImpl implements AuthUseCase {

    private PasswordEncoder passwordEncoder;

    private JwtUtils jwtUtils;

    private UserDetailsService userDetailsUseCase;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserDetailsService userDetailsUseCase){
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsUseCase = userDetailsUseCase;
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        String username = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accestoken = jwtUtils.createToken(authentication);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, ResponseMessagesConstants.LOGIN_EXITOSO, accestoken, true);

        return authResponseDTO;
    }

    @Override
    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = userDetailsUseCase.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException(ExceptionMessagesConstants.CREDENCIALES_INVALIDAS);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(ExceptionMessagesConstants.CREDENCIALES_INVALIDAS);
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(),
                        userDetails.getAuthorities());
    }

}

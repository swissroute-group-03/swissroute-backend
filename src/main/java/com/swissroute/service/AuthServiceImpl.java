package com.swissroute.service;

import com.swissroute.constant.ExceptionMessagesConstants;
import com.swissroute.constant.ResponseMessagesConstants;
import com.swissroute.model.User;
import com.swissroute.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swissroute.dto.request.AuthRequestDTO;
import com.swissroute.dto.response.AuthResponseDTO;
import com.swissroute.service.use_cases.AuthUseCase;
import com.swissroute.util.JwtUtils;

@Service
public class AuthServiceImpl implements AuthUseCase {

    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsUseCase;
    private final UserRepository userRepository;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserDetailsService userDetailsUseCase, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsUseCase = userDetailsUseCase;
        this.userRepository = userRepository;
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        String username = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException(ExceptionMessagesConstants.CREDENCIALES_INVALIDAS));

        String accestoken = jwtUtils.createToken(authentication, user.getId());

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

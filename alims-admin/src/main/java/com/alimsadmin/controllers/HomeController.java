package com.alimsadmin.controllers;

import com.alimsadmin.dto.JWT.AuthRequest;
import com.alimsadmin.dto.JWT.AuthResponse;
import com.alimsadmin.service.JWT.CustomUserDetailsService;
import com.alimsadmin.utils.JWT.JwtUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/admin/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        AuthResponse authResponse = new AuthResponse();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            authResponse.setErrorMessege("INVALID_CREDENTIALS :" + e);
            return authResponse;
        }
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtility.generateToken(userDetails);
        authResponse.setJwtToken(token);
        LOGGER.info("/****** HomeController -> authenticate() successful! " + token);
        return authResponse;
    }

}

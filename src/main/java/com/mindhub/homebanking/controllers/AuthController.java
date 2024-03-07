package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.services.AuthVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthVerificationService authVerificationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO)  {
        return authVerificationService.verifyLogin(loginDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO){
        return authVerificationService.verifyRegistration(registerDTO);
    }

}

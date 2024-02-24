package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){

        try{

            if (loginDTO.email().isBlank()){
                return new ResponseEntity<>("Email has no content", HttpStatus.BAD_REQUEST);
            }

            if (!loginDTO.email().contains("@")){
                return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
            }

            if (loginDTO.password().isBlank()){
                return new ResponseEntity<>("Password has no content", HttpStatus.BAD_REQUEST);
            }

            authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
            final  String jwt = jwtUtilService.generateToken(userDetails);

            return  ResponseEntity.ok(jwt);

        }catch (Exception e){

            return new ResponseEntity<>("Username y/o password incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO){

        if (registerDTO.email().isBlank()){
            return new ResponseEntity<>("Email has no content", HttpStatus.BAD_REQUEST);
        }

        if (!registerDTO.email().contains("@")){
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }

        if(clientRepository.findByEmail(registerDTO.email()) != null){
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.password().isBlank()){
            return new ResponseEntity<>("Password has no content", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.password().length() < 8){
            return new ResponseEntity<>("Enter a password longer than 8 digits", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.name().isBlank()){
             return new ResponseEntity<>("Name has no content", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.lastName().isBlank()){
            return new ResponseEntity<>("Lastname has no content", HttpStatus.BAD_REQUEST);
        }

        Client newClient = new Client(registerDTO.name(),
                                        registerDTO.lastName(),
                                        registerDTO.email(),
                                        passwordEncoder.encode(registerDTO.password()));
        clientRepository.save(newClient);

        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public  ResponseEntity<?> test(){
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("Hello " + mail);
    }
}

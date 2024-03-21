package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.securityServices.JwtUtilService;
import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthVerificationService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    public ResponseEntity<?> verifyLogin(LoginDTO loginDTO){

        if (loginDTO.email().isBlank()){
            return new  ResponseEntity<>("Email has no content", HttpStatus.BAD_REQUEST);
        }

        if (!loginDTO.email().contains("@")){
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }

        if (loginDTO.password().isBlank()){
            return new ResponseEntity<>("Password has no content", HttpStatus.BAD_REQUEST);
        }

        if (!clientService.clientExistsByEmail(loginDTO.email())){
            return new ResponseEntity<>("Email not registered", HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(loginDTO.password(), clientService.getClientByEmail(loginDTO.email()).getPassword())){
            return new ResponseEntity<>("Password incorrect", HttpStatus.UNAUTHORIZED);
        }

        authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
        final  String jwt = jwtUtilService.generateToken(userDetails);

        return  ResponseEntity.ok(jwt);
    }

    public ResponseEntity<?> verifyRegistration(RegisterDTO registerDTO){

        if (registerDTO.name().isBlank()){
            return new ResponseEntity<>("Name has no content", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.lastName().isBlank()){
            return new ResponseEntity<>("Lastname has no content", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.email().isBlank()){
            return new ResponseEntity<>("Email has no content", HttpStatus.BAD_REQUEST);
        }

        if (!registerDTO.email().contains("@")){
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }

        if(clientService.clientExistsByEmail(registerDTO.email())){
            return new ResponseEntity<>("Email is already registered", HttpStatus.FORBIDDEN);
        }

        if (registerDTO.password().isBlank()){
            return new ResponseEntity<>("Password has no content", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.password().length() < 8){
            return new ResponseEntity<>("Enter a password longer than 8 digits", HttpStatus.BAD_REQUEST);
        }

        Client newClient = new Client(registerDTO.name(),
                registerDTO.lastName(),
                registerDTO.email(),
                passwordEncoder.encode(registerDTO.password()));


        GenerateRandomNum generateRandomNumAccount = new GenerateRandomNum();

        String numNewAccount;

        do{
            numNewAccount = generateRandomNumAccount.getRandomNumberAccount();
        }while (accountService.accountExistsByNumber(numNewAccount));

        Account newAccount = new Account(numNewAccount , LocalDate.now(), 0.0);

        newClient.addAccount(newAccount);
        clientService.saveClient(newClient);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
        }
}

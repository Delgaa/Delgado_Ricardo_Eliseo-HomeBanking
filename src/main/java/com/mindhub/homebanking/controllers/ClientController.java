package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.ClientVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientVerificationService clientVerificationService;


    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getClients(){
        return new ResponseEntity<>(clientService.getAllClientsDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){
        return clientVerificationService.verifyClientId(id);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(){
        return clientVerificationService.verifyClientAccess();
    }
}

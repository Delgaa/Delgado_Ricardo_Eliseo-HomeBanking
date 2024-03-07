package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ClientVerificationService {

    @Autowired
    private ClientService clientService;

    public ResponseEntity<?> verifyClientId(Long id){
        Client client = clientService.getClientById(id);

        return client != null ? new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    public ResponseEntity<?> verifyClientAccess(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        return ResponseEntity.ok(new ClientDTO(client));
    }
}



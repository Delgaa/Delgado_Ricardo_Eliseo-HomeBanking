package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getClients(){
        return new ResponseEntity<>(clientService.getAllClientsDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){
        Client client = clientService.getClientById(id);

        return client != null ? new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        return ResponseEntity.ok(new ClientDTO(client));
    }
}

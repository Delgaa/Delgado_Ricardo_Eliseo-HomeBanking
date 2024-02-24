package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import java.util.stream.Collectors;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
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
    private ClientRepository clientRepository;

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getClients(){
        List<Client> clients =clientRepository.findAll();
        return new ResponseEntity<>(clients.stream().map(ClientDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);

        return client != null ? new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        return ResponseEntity.ok(new ClientDTO(client));
    }
}

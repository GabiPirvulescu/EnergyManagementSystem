package com.energymgmt.user_service.controllers;

import com.energymgmt.user_service.dtos.UserDTO;
import com.energymgmt.user_service.dtos.UserDetailsDTO;
import com.energymgmt.user_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<UserDTO>> getUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserDetailsDTO user){
        UUID id = userService.insertUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserDetailsDTO> updateUser(@PathVariable("id") UUID id,
                                                     @Valid @RequestBody UserDetailsDTO userDetailsDTO) {
        UserDetailsDTO updatedUser = userService.updateUser(id, userDetailsDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
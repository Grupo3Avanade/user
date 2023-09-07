package com.avanade.user.controllers;

import com.avanade.user.payloads.request.RequestUser;
import com.avanade.user.payloads.response.ResponseUser;
import com.avanade.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseUser> create(@RequestBody @Valid RequestUser request) {
        ResponseUser response = service.create(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUser>> findAll() {
        List<ResponseUser> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUser> findById(@PathVariable UUID id) {
        ResponseUser response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUser> update(@PathVariable UUID id, @RequestBody @Valid RequestUser request) {
        ResponseUser response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.KAT_web.social.controllers;

import com.KAT_web.social.models.Users;
import com.KAT_web.social.services.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) { this.usersService = usersService; }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        return ResponseEntity.ok(usersService.saveUser(user));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody Users user) {
        if (usersService.getUserByUsername(user.getUsername()).isPresent()
                || usersService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "That username or email is already in use."));
        }
        return ResponseEntity.ok(usersService.saveUser(user));
    }

    @GetMapping("/all")
    public List<Users> getUsers() { return usersService.getAllUsers(); }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return usersService.getUserById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        return usersService.getUserByUsername(username).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        return usersService.getUserByEmail(email).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<Users> loginUser(@RequestBody Users credentials) {
        return usersService.getUserByUsername(credentials.getUsername())
                .filter(user -> credentials.getPassword() != null && credentials.getPassword().equals(user.getPassword()))
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (usersService.getUserById(id).isEmpty()) return ResponseEntity.notFound().build();
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

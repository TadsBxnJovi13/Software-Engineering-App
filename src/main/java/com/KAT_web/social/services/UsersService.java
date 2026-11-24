package com.KAT_web.social.services;

import com.KAT_web.social.models.Users;
import com.KAT_web.social.repository.UsersRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepo usersRepo;

    public UsersService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Users saveUser(Users user) { return usersRepo.save(user); }
    public List<Users> getAllUsers() { return usersRepo.findAll(); }
    public Optional<Users> getUserById(Long id) { return usersRepo.findById(id); }
    public Optional<Users> getUserByUsername(String username) { return usersRepo.findByUsername(username); }
    public Optional<Users> getUserByEmail(String email) { return usersRepo.findByEmail(email); }
    public void deleteUser(Long id) { usersRepo.deleteById(id); }
}

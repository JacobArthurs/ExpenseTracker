package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.UserLoginRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.UserRegisterRequestDto;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public UserService(UserRepository userRepository, CurrentUserProvider currentUserProvider) {
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers () {
        return userRepository.findAll();
    }

    public Boolean validateLogin(UserLoginRequestDto request) {
        var user = userRepository.findByUsername(request.getUserName().toLowerCase());

        if (user.isPresent())
            return new BCryptPasswordEncoder().matches(request.getPassword(), user.get().getPassword());

        return false;
    }

    public User registerUser(UserRegisterRequestDto request) {
        var username = request.getUserName().toLowerCase();
        var currentUser = currentUserProvider.getCurrentUser();
        if (!currentUser.getUsername().equals(username) && currentUser.getRole() != UserRole.ADMIN){
            throw new RuntimeException("You are not authorized to update a user that is not yourself.");
        }

        var isPresent = userRepository.findByUsername(username).isPresent();
        if (isPresent)
            throw new RuntimeException("Username '" + username + "' is already taken.");

        var now = new Timestamp(System.currentTimeMillis());

        var user = new User(
                username,
                request.getPassword(),
                request.getName(),
                request.getEmail(),
                UserRole.DEFAULT,
                now,
                now
        );

        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public User updateUserRole(Long id, UserRole role) {
        if (currentUserProvider.getCurrentUser().getRole() != UserRole.ADMIN)
            throw new RuntimeException("You are not authorized to update roles");

        var user = userRepository.findById(id);
        if (user.isEmpty())
            throw new RuntimeException("User not found with ID: " + id);

        var updateUser = user.get();
        updateUser.setRole(role);

        return userRepository.save(updateUser);
    }

    public User updateUser(Long id, UserRegisterRequestDto request) {
        var user = userRepository.findById(id);

        if (user.isPresent()) {
            var now = new Timestamp(System.currentTimeMillis());

            var updateUser = new User(
                    request.getUserName().toLowerCase(),
                    request.getPassword(),
                    request.getName(),
                    request.getEmail(),
                    UserRole.DEFAULT,
                    user.get().getCreatedDate(),
                    now
            );

            return userRepository.save(updateUser);
        } else
            throw new RuntimeException("User not found with ID: " + id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username.toLowerCase())
                .orElse(null);
    }
}

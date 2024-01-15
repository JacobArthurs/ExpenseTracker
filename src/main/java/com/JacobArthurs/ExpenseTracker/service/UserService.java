package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.UserLoginRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.UserRegisterRequestDto;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers () {
        return userRepository.findAll();
    }

    public Boolean validateLogin(UserLoginRequestDto request) {
        var user = userRepository.findByUsername(request.getUserName());

        if (user.isPresent())
            return new BCryptPasswordEncoder().matches(request.getPassword(), user.get().getPassword());

        return false;
    }

    public User registerUser(UserRegisterRequestDto request) {
        var user = new User(
                request.getUserName(),
                request.getPassword(),
                request.getName(),
                request.getEmail(),
                new Timestamp(System.currentTimeMillis())
        );

        return userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}

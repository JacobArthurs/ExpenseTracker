package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.OperationResult;
import com.JacobArthurs.ExpenseTracker.dto.UserLoginRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.UserRegisterRequestDto;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.event.UserCreatedEvent;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, CurrentUserProvider currentUserProvider, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return The user with the specified ID, or null if not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Validates user login credentials.
     *
     * @param request The login request containing username and password
     * @return True if the login credentials are valid, otherwise false
     */
    public Boolean validateLogin(UserLoginRequestDto request) {
        var user = userRepository.findByUsername(request.getUserName().toLowerCase());

        return user.map(u -> new BCryptPasswordEncoder().matches(request.getPassword(), u.getPassword())).orElse(false);
    }

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details
     * @return An operation result indicating success or failure
     */
    public OperationResult registerUser(UserRegisterRequestDto request) {
        var username = request.getUserName().toLowerCase();

        if (userRepository.findByUsername(username).isPresent())
            return new OperationResult(false, "Username '" + username + "' is already taken.");

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

        var newUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserCreatedEvent(newUser));

        return new OperationResult(true, "User registered successfully");
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete
     * @return True if the user was deleted successfully, otherwise false
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the role of a user.
     *
     * @param id   The ID of the user to update
     * @param role The new role for the user
     * @return The updated user
     * @throws RuntimeException If the current user is not authorized or the user to update is not found
     */
    public User updateUserRole(Long id, UserRole role) {
        if (currentUserProvider.getCurrentUser().getRole() != UserRole.ADMIN)
            throw new RuntimeException("You are not authorized to update roles");

        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * Updates a user's information.
     *
     * @param id      The ID of the user to update
     * @param request The updated user information
     * @return The updated user
     * @throws RuntimeException If the current user is not authorized or the user to update is not found
     */
    public User updateUser(Long id, UserRegisterRequestDto request) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        var username = request.getUserName().toLowerCase();
        var currentUser = currentUserProvider.getCurrentUser();

        if (!currentUser.getUsername().equals(username) && currentUser.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You are not authorized to update a user that is not yourself.");
        }

        var now = new Timestamp(System.currentTimeMillis());

        user.setUsername(username);
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLastUpdatedDate(now);

        return userRepository.save(user);
    }

    /**
     * Loads a user by their username.
     *
     * @param username The username of the user to load
     * @return The UserDetails object representing the user
     * @throws UsernameNotFoundException If no user with the specified username is found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}

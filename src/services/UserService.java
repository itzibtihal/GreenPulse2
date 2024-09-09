package services;

import entities.User;
import exceptions.DatabaseConnectionException;
import exceptions.InvalidInputException;
import exceptions.UserNotFoundException;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public Optional<User> findById(UUID id) {
        validateId(id);
        return userRepository.findById(id);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        validateUser(user);
        userRepository.save(user);
    }

    public Optional<User> delete(UUID id) {
        validateId(id);
        return userRepository.delete(id);
    }


    public Optional<User> update(UUID id, String newName, Integer newAge) {
        validateId(id);
        if (newName != null) {
            validateName(newName);
        }
        if (newAge != null) {
            validateAge(newAge);
        }
        return userRepository.update(id, newName, newAge);
    }


    public Optional<Double> findMaxCarbonConsumption(UUID userId) {
        validateId(userId);
        return userRepository.findMaxCarbonConsumption(userId);
    }



    // Validate user ID
    private void validateId(UUID id) {
        if (id == null) {
            throw new InvalidInputException("User ID cannot be null.");
        }
    }

    // Validate user object
    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidInputException("User cannot be null.");
        }
        validateName(user.getName());
        validateAge(user.getAge());
    }

    // Validate user's name
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("User name cannot be null or empty.");
        }
        if (name.length() < 2) {
            throw new InvalidInputException("User name must be at least 2 characters long.");
        }
    }

    // Validate user's age
    private void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidInputException("Invalid age. Age must be between 0 and 150.");
        }
    }
}

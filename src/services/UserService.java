package services;

import entities.User;
import exceptions.DatabaseConnectionException;
import exceptions.InvalidInputException;
import exceptions.UserNotFoundException;
import repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    // exo ;  filter userbyage

    public List<User> listByAge(int ageThreshold) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getAge() > ageThreshold)
                .collect(Collectors.toList());
    }

   //get users with a specific name

    public List<User> listByName(String name) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> Objects.equals(user.getName(), name)).collect(Collectors.toList());
//        return users.stream()
//                .filter(user -> user.getName().equalsIgnoreCase(name))
//                .collect(Collectors.toList());
    }

    //findOldestUser

    public Optional<User> findOldestUser(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .max((user1 , user2)-> Integer.compare(user1.getAge(),user2.getAge()));
    }

    //calculateAverageAge
    public OptionalDouble calculateAverageAge(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .mapToInt(User::getAge)
                .average();
    }

    public List<User> findTopNOldestUsers(int n) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((user1, user2) -> Integer.compare(user2.getAge(), user1.getAge()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<User> filterAndSortUsersByAge(int minAge) {
        return userRepository.findAll().stream()
                .filter(user -> user.getAge() > minAge)
                .sorted(Comparator.comparingInt(User::getAge).reversed())
                .collect(Collectors.toList());
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

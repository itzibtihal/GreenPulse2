package services;

import entities.*;
import exceptions.ConsumptionNotFoundException;
import exceptions.InvalidConsumptionException;
import repositories.ConsumptionRepository;
import repositories.UserRepository;
import validators.ConsumptionValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ConsumptionService {

    private final ConsumptionRepository repository;
    private final UserRepository userRepository;

    public ConsumptionService(Connection connection) {
        this.repository = new ConsumptionRepository(connection);
        this.userRepository = new UserRepository();
    }

    public void saveConsumption(Consumption consumption) throws SQLException {
        ConsumptionValidator.validateConsumption(consumption);
        repository.save(consumption);
    }

    public void deleteConsumption(UUID id) throws SQLException {
        ConsumptionValidator.validateId(id);
        if (repository.findById(id).isEmpty()) {
            throw new ConsumptionNotFoundException("Consumption not found for ID: " + id);
        }
        repository.delete(id);
    }

    public Optional<Consumption> findConsumptionById(UUID id) throws SQLException {
        ConsumptionValidator.validateId(id);
        Optional<Consumption> consumption = repository.findById(id);
        if (consumption.isEmpty()) {
            throw new ConsumptionNotFoundException("Consumption not found for ID: " + id);
        }
        return consumption;
    }

    public List<Consumption> findAllConsumptions() throws SQLException {
        return repository.findAll();
    }

    public void saveAllConsumptions(List<Consumption> consumptions) throws SQLException {
        if (consumptions == null || consumptions.isEmpty()) {
            throw new InvalidConsumptionException("Consumptions list cannot be null or empty");
        }

        for (Consumption consumption : consumptions) {
            ConsumptionValidator.validateConsumption(consumption);
        }

        for (Consumption consumption : consumptions) {
            repository.save(consumption);
        }
    }

    public List<Consumption> findConsumptionsByUserId(UUID userId) throws SQLException {
        ConsumptionValidator.validateId(userId);
        return repository.findConsumptionsByUserId(userId);
    }

    public Map<User, Map<ConsumptionType, List<Consumption>>> findAllUsersWithConsumptions() throws SQLException {
        return repository.findAllUsersWithConsumptions();
    }



    // filtration
    public List<User> findUsersExceedingCarbonThreshold(double threshold) throws SQLException {
        List<User> allUsers = userRepository.findAll(); // Method to retrieve all users
        List<User> usersExceedingThreshold = new ArrayList<>();

        for (User user : allUsers) {
            double totalCarbonImpact = calculateTotalCarbonImpactForUser(user.getId());
            if (totalCarbonImpact > threshold) {
                usersExceedingThreshold.add(user);
            }
        }

        return usersExceedingThreshold;
    }

    private double calculateTotalCarbonImpactForUser(UUID userId) throws SQLException {
        List<Consumption> consumptions = repository.findConsumptionsByUserId(userId);

        return consumptions.stream()
                .mapToDouble(Consumption::calculateImpact)
                .sum();
    }


}

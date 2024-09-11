package services;

import entities.Consumption;
import entities.ConsumptionType;
import entities.Food;
import entities.Housing;
import entities.Transport;
import exceptions.ConsumptionNotFoundException;
import exceptions.InvalidConsumptionException;
import repositories.ConsumptionRepository;
import validators.ConsumptionValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConsumptionService {

    private final ConsumptionRepository repository;

    public ConsumptionService(Connection connection) {
        this.repository = new ConsumptionRepository(connection);
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
}

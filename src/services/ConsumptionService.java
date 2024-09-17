package services;

import entities.*;
import exceptions.ConsumptionNotFoundException;
import exceptions.InvalidConsumptionException;
import exceptions.UserNotFoundException;
import repositories.ConsumptionRepository;
import repositories.UserRepository;
import validators.ConsumptionValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ConsumptionService {

    private final ConsumptionRepository repository;
    private final UserRepository userRepository;

    public ConsumptionService(Connection connection) {
        this.repository = new ConsumptionRepository(connection);
        this.userRepository = new UserRepository();
    }

    public void save(Consumption consumption) throws SQLException {
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
        List<User> allUsers = userRepository.findAll();
        List<User> usersExceedingThreshold = new ArrayList<>();

        for (User user : allUsers) {
            double totalCarbonImpact = calculateTotalCarbonImpactForUser(user.getId());
            if (totalCarbonImpact > threshold) {
                usersExceedingThreshold.add(user);
            }
        }
//        usersExceedingThreshold = allUsers.stream()
//                .filter(user -> calculateTotalCarbonImpactForUser(user.getId()) > threshold)
//                .collect(Collectors.toList());

        return usersExceedingThreshold;
    }

    public double calculateTotalCarbonImpactForUser(UUID userId) throws SQLException {
        List<Consumption> consumptions = repository.findConsumptionsByUserId(userId);

        return consumptions.stream()
                .mapToDouble(Consumption::calculateImpact)
                .sum();
    }



    public List<User> findInactiveUsers(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<User> allUsers = userRepository.findAll();
        List<User> inactiveUsers = new ArrayList<>();

        for (User user : allUsers) {
            List<Consumption> consumptions = repository.findConsumptionsByUserId(user.getId());

            boolean isActive = consumptions.stream()
                    .anyMatch(consumption -> isWithinPeriod(consumption, startDate, endDate));

            if (!isActive) {
                inactiveUsers.add(user);
            }
        }

        return inactiveUsers;
    }



    private boolean isWithinPeriod(Consumption consumption, LocalDate startDate, LocalDate endDate) {
        return !(consumption.getStartDate().isAfter(endDate) || consumption.getEndDate().isBefore(startDate));
    }



    public List<User> sortUsersByCarbonConsumption() throws SQLException {
        List<User> allUsers = userRepository.findAll();

        List<User> sortedUsers = allUsers.stream()
                .sorted((user1, user2) -> {
                    try {

                        double carbonImpact1 = calculateTotalCarbonImpactForUser(user1.getId());
                        double carbonImpact2 = calculateTotalCarbonImpactForUser(user2.getId());

                        return Double.compare(carbonImpact2, carbonImpact1);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error calculating carbon impact", e);
                    }
                })
                .collect(Collectors.toList());

        return sortedUsers;
    }



  public Map<User, Double> calculateAverageCarbonConsumptionPerUser(LocalDate startDate, LocalDate endDate) throws SQLException {

    List<User> allUsers = userRepository.findAll();
    Map<User, Double> averageConsumptionMap = new HashMap<>();

    for (User user : allUsers) {
        List<Consumption> consumptions = repository.findConsumptionsByUserId(user.getId()).stream()
                .filter(consumption -> isWithinPeriod(consumption, startDate, endDate))
                .collect(Collectors.toList());

        if (!consumptions.isEmpty()) {
            double totalConsumption = consumptions.stream()
                    .mapToDouble(Consumption::calculateImpact)
                    .sum();

            double averageConsumption = totalConsumption / consumptions.size();
            averageConsumptionMap.put(user, averageConsumption);
        } else {
            averageConsumptionMap.put(user, 0.0);
        }
    }
    return averageConsumptionMap;
  }


    public Map<User, Map<ConsumptionType, List<Consumption>>> getUserDetailsWithConsumptions(UUID userId) throws SQLException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));
        List<Consumption> consumptions = repository.findConsumptionsByUserId(userId);

        Map<ConsumptionType, List<Consumption>> consumptionsByType = consumptions.stream()
                .collect(Collectors.groupingBy(Consumption::getType));

        Map<User, Map<ConsumptionType, List<Consumption>>> result = new HashMap<>();
        result.put(user, consumptionsByType);

        return result;
    }

}


//    public double calculateAverageConsumption(UUID userId, LocalDate startDate, LocalDate endDate) {
//    User user = findUserById(userId);
//    List<Consommation> consommations = user.getConsommations();
//    List<Consommation> consommationsInPeriod = consommations.stream()
//            .filter(c -> c.getDate().isAfter(startDate.minusDays(1)) && c.getDate().isBefore(endDate.plusDays(1)))
//            .collect(Collectors.toList());
//
//    if (consommationsInPeriod.isEmpty()) {
//        throw new InvalidConsumptionException("Aucune consommation trouvée dans la période donnée.");
//    }
//
//    double totalCarbonConsumption = consommationsInPeriod.stream()
//            .mapToDouble(Consommation::getCarbonImpact)
//            .sum();
//
//    return totalCarbonConsumption / consommationsInPeriod.size();
//}
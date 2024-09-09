package repositories;

import entities.*;
import entities.ConsumptionType;
import entities.enums.EnergyType;
import entities.enums.FoodType;
import entities.enums.VehicleType;

import exceptions.ConsumptionNotFoundException;
import exceptions.InvalidConsumptionException;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.sql.Date;


public class ConsumptionRepository {

    private final Connection connection;

    public ConsumptionRepository(Connection connection) {
        this.connection = connection;
    }

    // Save or update consumption
    public void save(Consumption consumption) throws SQLException {
        String query = "INSERT INTO consumption (id, user_id, start_date, end_date, amount, type, impact) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "user_id = EXCLUDED.user_id, start_date = EXCLUDED.start_date, end_date = EXCLUDED.end_date, " +
                "amount = EXCLUDED.amount, type = EXCLUDED.type, impact = EXCLUDED.impact";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, consumption.getId());
            statement.setObject(2, consumption.getUserId());
            statement.setDate(3, Date.valueOf(consumption.getStartDate()));
            statement.setDate(4, Date.valueOf(consumption.getEndDate()));
            statement.setDouble(5, consumption.getAmount());
            statement.setString(6, consumption.getType().name());
            statement.setDouble(7, consumption.calculateImpact());
            statement.executeUpdate();

            if (consumption instanceof Transport) {
                saveTransport((Transport) consumption);
            } else if (consumption instanceof Housing) {
                saveHousing((Housing) consumption);
            } else if (consumption instanceof Food) {
                saveFood((Food) consumption);
            }
        }
    }

    // Save transport consumption
    private void saveTransport(Transport transport) throws SQLException {
        String query = "INSERT INTO transport (consumption_id, distanceTraveled, vehicleType) VALUES (?, ?, ?) " +
                "ON CONFLICT (consumption_id) DO UPDATE SET " +
                "distanceTraveled = EXCLUDED.distanceTraveled, vehicleType = EXCLUDED.vehicleType";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, transport.getId());
            statement.setDouble(2, transport.getDistanceTraveled());
            statement.setString(3, transport.getVehicleType().name());
            statement.executeUpdate();
        }
    }

    // Save housing consumption
    private void saveHousing(Housing housing) throws SQLException {
        String query = "INSERT INTO housing (consumption_id, energyConsumption, energyType) VALUES (?, ?, ?) " +
                "ON CONFLICT (consumption_id) DO UPDATE SET " +
                "energyConsumption = EXCLUDED.energyConsumption, energyType = EXCLUDED.energyType";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, housing.getId());
            statement.setDouble(2, housing.getEnergyConsumption());
            statement.setString(3, housing.getEnergyType().name());
            statement.executeUpdate();
        }
    }

    // Save food consumption
    private void saveFood(Food food) throws SQLException {
        String query = "INSERT INTO food (consumption_id, weight, foodType) VALUES (?, ?, ?) " +
                "ON CONFLICT (consumption_id) DO UPDATE SET " +
                "weight = EXCLUDED.weight, foodType = EXCLUDED.foodType";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, food.getId());
            statement.setDouble(2, food.getWeight());
            statement.setString(3, food.getFoodType().name());
            statement.executeUpdate();
        }
    }

    // Find a consumption by its ID
    public Optional<Consumption> findById(UUID id) {
        String query = "SELECT * FROM consumption WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ConsumptionType type = ConsumptionType.valueOf(resultSet.getString("type"));
                    UUID userId = UUID.fromString(resultSet.getString("user_id"));
                    LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                    LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
                    double amount = resultSet.getDouble("amount");
                    double impact = resultSet.getDouble("impact");

                    return Optional.of(createConsumption(type, id, userId, startDate, endDate, amount, impact));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding consumption by ID", e);
        }
        return Optional.empty();
    }

    private Consumption createConsumption(entities.ConsumptionType type, UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double impact) throws SQLException {
        switch (type) {
            case TRANSPORT:
                return findTransportById(id, userId, startDate, endDate, amount, impact);
            case HOUSING:
                return findHousingById(id, userId, startDate, endDate, amount, impact);
            case FOOD:
                return findFoodById(id, userId, startDate, endDate, amount, impact);
            default:
                throw new InvalidConsumptionException("Unknown consumption type");
        }
    }


    private Transport findTransportById(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double impact) throws SQLException {
        String query = "SELECT * FROM transport t " +
                "JOIN consumption c ON t.consumption_id = c.id " +
                "WHERE t.consumption_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double distanceTraveled = resultSet.getDouble("distance_traveled");
                    VehicleType vehicleType = VehicleType.valueOf(resultSet.getString("vehicle_type"));
                    return new Transport(id, userId, startDate, endDate, amount, distanceTraveled, vehicleType);
                }
            }
        }
        throw new ConsumptionNotFoundException("Transport consumption not found for ID: " + id);
    }

    private Housing findHousingById(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double impact) throws SQLException {
        String query = "SELECT * FROM housing h " +
                "JOIN consumption c ON h.consumption_id = c.id " +
                "WHERE h.consumption_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double energyConsumption = resultSet.getDouble("energy_consumption");
                    EnergyType energyType = EnergyType.valueOf(resultSet.getString("energy_type"));
                    return new Housing(id, userId, startDate, endDate, amount, energyConsumption, energyType);
                }
            }
        }
        throw new ConsumptionNotFoundException("Housing consumption not found for ID: " + id);
    }

    private Food findFoodById(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double impact) throws SQLException {
        String query = "SELECT * FROM food f " +
                "JOIN consumption c ON f.consumption_id = c.id " +
                "WHERE f.consumption_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double weight = resultSet.getDouble("weight");
                    FoodType foodType = FoodType.valueOf(resultSet.getString("food_type"));
                    return new Food(id, userId, startDate, endDate, amount, weight, foodType);
                }
            }
        }
        throw new ConsumptionNotFoundException("Food consumption not found for ID: " + id);
    }



    // List all consumptions
    public List<Consumption> findAll() {
        String query = "SELECT id FROM consumption";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            return StreamSupport.stream(new ResultSetSpliterator(resultSet), false)
                    .map(row -> {
                        try {
                            UUID id = UUID.fromString(row.getString("id"));
                            return findById(id);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .flatMap(Optional::stream) // Convert Optional to Stream if present
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Database error while listing all consumptions", e);
        }
    }

    // Delete consumption by ID
    public void delete(UUID id) throws SQLException {
        String query = "DELETE FROM consumption WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        }
    }

    // Helper class to create a stream from ResultSet
    private static class ResultSetSpliterator implements Spliterator<ResultSet> {
        private final ResultSet resultSet;
        private boolean hasNext = true;

        public ResultSetSpliterator(ResultSet resultSet) {
            this.resultSet = resultSet;
        }

        @Override
        public boolean tryAdvance(Consumer<? super ResultSet> action) {
            if (hasNext) {
                try {
                    if (resultSet.next()) {
                        action.accept(resultSet);
                        return true;
                    } else {
                        hasNext = false;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error while iterating over ResultSet", e);
                }
            }
            return false;
        }

        @Override
        public Spliterator<ResultSet> trySplit() {
            return null; // Not supported
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE; // Unknown size
        }

        @Override
        public int characteristics() {
            return ORDERED | NONNULL | IMMUTABLE;
        }
    }
}

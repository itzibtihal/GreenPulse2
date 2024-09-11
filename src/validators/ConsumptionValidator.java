package validators;

import entities.Consumption;
import entities.Food;
import entities.Housing;
import entities.Transport;
import exceptions.InvalidConsumptionException;

import java.util.UUID;

public class ConsumptionValidator {

    public static void validateConsumption(Consumption consumption) {
        if (consumption == null) {
            throw new InvalidConsumptionException("Consumption cannot be null");
        }

        if (consumption.getId() == null) {
            throw new InvalidConsumptionException("Consumption ID cannot be null");
        }

        if (consumption.getUserId() == null) {
            throw new InvalidConsumptionException("User ID cannot be null");
        }

        if (consumption.getStartDate() == null || consumption.getEndDate() == null) {
            throw new InvalidConsumptionException("Start date and end date cannot be null");
        }

        if (consumption.getStartDate().isAfter(consumption.getEndDate())) {
            throw new InvalidConsumptionException("Start date cannot be after end date");
        }

        if (consumption.getAmount() < 0) {
            throw new InvalidConsumptionException("Amount cannot be negative");
        }

        if (consumption instanceof Transport) {
            validateTransport((Transport) consumption);
        } else if (consumption instanceof Housing) {
            validateHousing((Housing) consumption);
        } else if (consumption instanceof Food) {
            validateFood((Food) consumption);
        } else {
            throw new InvalidConsumptionException("Unknown consumption type");
        }
    }

    private static void validateTransport(Transport transport) {
        if (transport.getDistanceTraveled() < 0) {
            throw new InvalidConsumptionException("Distance traveled cannot be negative");
        }
    }

    private static void validateHousing(Housing housing) {
        if (housing.getEnergyConsumption() < 0) {
            throw new InvalidConsumptionException("Energy consumption cannot be negative");
        }
    }

    private static void validateFood(Food food) {
        if (food.getWeight() < 0) {
            throw new InvalidConsumptionException("Weight cannot be negative");
        }
    }

    public static void validateId(UUID id) {
        if (id == null) {
            throw new InvalidConsumptionException("Consumption ID cannot be null");
        }
    }
}

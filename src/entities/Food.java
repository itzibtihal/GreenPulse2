package entities;

import entities.enums.FoodType;

import java.time.LocalDate;
import java.util.UUID;

public class Food extends Consumption {
    private double weight;
    private FoodType foodType;

    // Constructor with parameters
    public Food(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double weight, FoodType foodType) {
        super(id, userId, startDate, endDate, amount, ConsumptionType.FOOD); // Pass FOOD type
        this.weight = weight;
        this.foodType = foodType;
    }

    // Default constructor
    public Food() {
    }

    // Getters and setters
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
    public double calculateImpact() {
        return weight * foodType.getCarbonImpact(); // Use the FoodType impact for calculation
    }

    @Override
    public String toString() {
        return "Food {" +
                "weight=" + weight +
                ", foodType=" + foodType +
                "} " + super.toString();
    }
}

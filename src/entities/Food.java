package entities;

import entities.enums.FoodType;
import java.time.LocalDate;

public class Food extends Consumption {
    private double weight;
    private FoodType foodType;

    public Food(LocalDate startDate, LocalDate endDate, double amount, double weight, FoodType foodType) {
        super(startDate, endDate, amount);
        this.weight = weight;
        this.foodType = foodType;
    }

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
    public double calculerImpact() {
        return weight * foodType.getCarbonImpact();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nFood Type: " + foodType +
                "\nWeight: " + weight ;
    }
}

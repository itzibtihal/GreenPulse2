package entities;

import entities.enums.EnergyType;

import java.time.LocalDate;
import java.util.UUID;

public class Housing extends Consumption {
    private double energyConsumption;
    private EnergyType energyType;

    // Constructor with parameters
    public Housing(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double energyConsumption, EnergyType energyType) {
        super(id, userId, startDate, endDate, amount, ConsumptionType.HOUSING); // Pass HOUSING type
        this.energyConsumption = energyConsumption;
        this.energyType = energyType;
    }

    // Default constructor
    public Housing() {
    }

    // Getters and setters
    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    @Override
    public double calculateImpact() {
        return energyConsumption * energyType.getCarbonImpact(); // Use the EnergyType impact for calculation
    }

    @Override
    public String toString() {
        return "Housing {" +
                "energyConsumption=" + energyConsumption +
                ", energyType=" + energyType +
                "} " + super.toString();
    }
}

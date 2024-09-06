package entities;

import entities.enums.EnergyType;
import java.time.LocalDate;

public class Housing extends Consumption {
    private double energyConsumption;
    private EnergyType energyType;

    public Housing(LocalDate startDate, LocalDate endDate, double amount, double energyConsumption, EnergyType energyType) {
        super(startDate, endDate, amount);
        this.energyConsumption = energyConsumption;
        this.energyType = energyType;
    }

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
    public double calculerImpact() {
        return energyConsumption * energyType.getCarbonImpact();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nEnergy Type: " + energyType +
                "\nEnergy Consumption: " + energyConsumption;
    }
}

package entities;

import entities.enums.VehicleType;

import java.time.LocalDate;

public class Transport extends Consumption {
    private double distanceTraveled;
    private VehicleType vehicleType;

    public Transport(LocalDate startDate, LocalDate endDate, double amount, double distanceTraveled, VehicleType vehicleType) {
        super(startDate, endDate, amount);
        this.distanceTraveled = distanceTraveled;
        this.vehicleType = vehicleType;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double calculateImpact() {
        return distanceTraveled * vehicleType.getCarbonImpact();
    }

    @Override
    public double calculerImpact() {
        return distanceTraveled * vehicleType.getCarbonImpact();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nVehicle Type: " + vehicleType +
                "\nDistance Traveled: " + distanceTraveled +
                "\nCarbon Impact: " + calculateImpact();
    }
}

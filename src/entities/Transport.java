package entities;

import entities.enums.VehicleType;

import java.time.LocalDate;
import java.util.UUID;

public class Transport extends Consumption {
    private double distanceTraveled;
    private VehicleType vehicleType;


    public Transport(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, double distanceTraveled, VehicleType vehicleType) {
        super(id, userId, startDate, endDate, amount, ConsumptionType.TRANSPORT); // Pass TRANSPORT type
        this.distanceTraveled = distanceTraveled;
        this.vehicleType = vehicleType;
    }


    public Transport() {
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

    @Override
    public double calculateImpact() {
        return distanceTraveled * vehicleType.getCarbonImpact();
    }

    @Override
    public String toString() {
        return "Transport {" +
                "distanceTraveled=" + distanceTraveled +
                ", vehicleType=" + vehicleType +
                "} " + super.toString();
    }
}

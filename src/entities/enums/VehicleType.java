package entities.enums;

public enum VehicleType {
    CAR(0.5),
    TRAIN(0.1);

    private final double carbonImpact;

    VehicleType(double carbonImpact) {
        this.carbonImpact = carbonImpact;
    }

    public double getCarbonImpact() {
        return carbonImpact;
    }
}

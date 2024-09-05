package entities.enums;

public enum EnergyType {
    ELECTRICITY(1.5),
    GAS(2.0);

    private final double carbonImpact;

    EnergyType(double carbonImpact) {
        this.carbonImpact = carbonImpact;
    }

    public double getCarbonImpact() {
        return carbonImpact;
    }
}

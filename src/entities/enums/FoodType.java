package entities.enums;

public enum FoodType {
    MEAT(5.0),
    VEGETABLE(0.5);

    private final double carbonImpact;

    FoodType(double carbonImpact) {
        this.carbonImpact = carbonImpact;
    }

    public double getCarbonImpact() {
        return carbonImpact;
    }
}

package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public abstract class Consumption {
    private UUID id;
    private UUID userId; // foreign key to users table
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;
    private ConsumptionType type; // ENUM in SQL
    private double impact;

    // Constructor with parameters
    public Consumption(UUID id, UUID userId, LocalDate startDate, LocalDate endDate, double amount, ConsumptionType type) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.type = type;
    }

    // Default constructor
    public Consumption() {
        this.id = UUID.randomUUID(); // Generate a random UUID by default
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public ConsumptionType getType() {
        return type;
    }

    public void setType(ConsumptionType type) {
        this.type = type;
    }

    // Method to calculate duration in days
    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    // Abstract method that subclasses must implement
    public abstract double calculateImpact();

    // toString method
    @Override
    public String toString() {
        return "Consumption {" +
                "id=" + id +
                ", userId=" + userId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", type=" + type +
                ", duration=" + getDurationInDays() + " days" +
                ", impact=" + calculateImpact() +
                '}';
    }
}


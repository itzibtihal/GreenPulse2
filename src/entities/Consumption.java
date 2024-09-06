package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Consumption {
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;

    // Constructeur avec paramètres
    public Consumption(LocalDate startDate, LocalDate endDate, double amount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    // Constructeur par défaut
    public Consumption() {
    }

    // Getters et setters
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
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Méthode pour calculer la durée en jours
    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    // Méthode abstraite que les sous-classes doivent implémenter
    public abstract double calculerImpact();

    // Méthode toString
    @Override
    public String toString() {
        return "CarbonConsumption : \n" +
                "\nstartDate=" + startDate +
                "\nendDate=" + endDate +
                "\namount=" + amount +
                "\nduration=" + getDurationInDays() + " days" +
                "\nimpact=" + calculerImpact(); // Inclut l'impact calculé
    }
}

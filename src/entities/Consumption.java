package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Consumption {
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;

    public Consumption(LocalDate startDate, LocalDate endDate, double amount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public Consumption() {
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
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    // calculate duration
    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }


    @Override
    public String toString() {
        return "CarbonConsumption : \n" +
                "\nstartDate=" + startDate +
                "\nendDate=" + endDate +
                "\namount=" + amount +
                "\nduration=" + getDurationInDays() + " days" ;
    }
}

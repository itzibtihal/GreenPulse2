package services;

import entities.Consumption;
import entities.User;
import exceptions.ConsumptionNotFoundException;
import repositories.UserRepository;

import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ReportService {

    private final UserService userService;
    private final ConsumptionService consumptionService;

    public ReportService(UserService userService, ConsumptionService consumptionService) {
        this.userService = userService;
        this.consumptionService = consumptionService;
    }

    public double calculateDailyConsumption(UUID userID, LocalDate reportDate) throws SQLException {
        User user = userService.findById(userID).orElseThrow(() -> new ConsumptionNotFoundException("User not found"));
        return user.getConsumption().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(reportDate) && !consumption.getEndDate().isBefore(reportDate))
                .mapToDouble(Consumption::calculateImpact)
                .sum();
    }


    public double calculateWeeklyConsumption(UUID userID, LocalDate reportDate) throws SQLException {
        User user = userService.findById(userID).orElseThrow(() -> new ConsumptionNotFoundException("User not found"));
        LocalDate weekStart = reportDate.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        return user.getConsumption().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(weekEnd) && !consumption.getEndDate().isBefore(weekStart))
                .mapToDouble(consumption -> {
                    long overlapDays = calculateOverlapDays(consumption, weekStart, weekEnd);
                    long totalDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return (consumption.calculateImpact() / totalDays) * overlapDays;
                })
                .sum();
    }


    public double calculateMonthlyConsumption(UUID userID, LocalDate reportDate) throws SQLException {
        User user = userService.findById(userID).orElseThrow(() -> new ConsumptionNotFoundException("User not found"));
        YearMonth month = YearMonth.from(reportDate);
        LocalDate monthStart = month.atDay(1);
        LocalDate monthEnd = month.atEndOfMonth();
        return user.getConsumption().stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(monthEnd) && !consumption.getEndDate().isBefore(monthStart))
                .mapToDouble(consumption -> {
                    long overlapDays = calculateOverlapDays(consumption, monthStart, monthEnd);
                    long totalDays = ChronoUnit.DAYS.between(consumption.getStartDate(), consumption.getEndDate()) + 1;
                    return (consumption.calculateImpact() / totalDays) * overlapDays;
                })
                .sum();
    }


    private long calculateOverlapDays(Consumption consumption, LocalDate start, LocalDate end) {
        LocalDate overlapStart = consumption.getStartDate().isAfter(start) ? consumption.getStartDate() : start;
        LocalDate overlapEnd = consumption.getEndDate().isBefore(end) ? consumption.getEndDate() : end;
        return ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;
    }

    public void generateReport(UUID userID, String reportType, LocalDate reportDate) throws SQLException {
        switch (reportType.toLowerCase()) {
            case "daily":
                double dailyConsumption = calculateDailyConsumption(userID, reportDate);
                System.out.printf("Daily carbon consumption report for %s for user %s: %.2f units%n",
                        reportDate,
                        userService.findById(userID).orElseThrow(() -> new ConsumptionNotFoundException("User not found")).getName(),
                        dailyConsumption);
                break;

            case "weekly":
                double weeklyConsumption = calculateWeeklyConsumption(userID, reportDate);
                System.out.printf("Weekly carbon consumption report for the week starting %s for user %s: %.2f units%n",
                        reportDate,
                        userService.findById(userID).orElseThrow(() -> new ConsumptionNotFoundException("User not found")).getName(),
                        weeklyConsumption);
                break;

            case "monthly":
                double monthlyConsumption = calculateMonthlyConsumption(userID, reportDate);
                System.out.printf("Monthly carbon consumption report for %s %d for user %s: %.2f units%n",
                        reportDate.getMonth(),
                        reportDate.getYear(),
                        userService.findById(userID).orElseThrow(() -> new ConsumptionNotFoundException("User not found")).getName(),
                        monthlyConsumption);
                break;

            default:
                System.out.println("Invalid report type.");
        }
    }
}

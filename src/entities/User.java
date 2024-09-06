package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private int age;
    private LocalDateTime dateOfCreation;
    private List<Consumption> consumption;

    public User(UUID id, String name, int age, LocalDateTime dateOfCreation) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.dateOfCreation = dateOfCreation;
        this.consumption = new ArrayList<>();
    }

    public User(String name, int age) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.dateOfCreation = LocalDateTime.now();
        this.consumption = new ArrayList<>();
    }

    public User() {
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Consumption> getConsumption() {
        return consumption;
    }

    public void setConsumption(List<Consumption> consumption) {
        this.consumption = consumption;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + ", dateOfCreation=" + dateOfCreation +
                ", consommations=" + consumption + "]";
    }
}

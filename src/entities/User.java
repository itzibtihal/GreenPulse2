package entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private int age;
    private LocalDateTime dateOfCreation;


    public User(String name, int age) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.dateOfCreation = LocalDateTime.now();
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


    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + ", dateOfCreation=" + dateOfCreation + "]";
    }
}

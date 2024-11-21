package com.example.main.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;
    private LocalDateTime creationDate;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    public enum Status {
        todo, doing, done
    }

    public enum Priority {
        low, medium, high
    }

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }
}

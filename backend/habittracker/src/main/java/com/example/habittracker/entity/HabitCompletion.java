package com.example.habittracker.entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "habit_completions",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_habit_completion_habit_data", columnNames = {"habit_id", "completion_date"})
    }
)
@Getter
@Setter
@NoArgsConstructor
public class HabitCompletion {
    
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "completion_date", nullable = false)
    private LocalDate date;

    @Column(name = "completion_at", nullable = false, updatable = false)
    private Instant completedAt;

    @PrePersist
    void prePersist() {
        this.completedAt = Instant.now();
    }
}

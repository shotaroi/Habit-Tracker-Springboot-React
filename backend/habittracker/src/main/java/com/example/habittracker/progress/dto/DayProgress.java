package com.example.habittracker.progress.dto;

import java.time.LocalDate;

public record DayProgress(
    LocalDate date,
    int completedHabits,
    int totalHabits
) {}

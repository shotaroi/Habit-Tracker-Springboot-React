package com.example.habittracker.habit.dto;

import java.time.Instant;

public record HabitResponse(
    Long id, 
    String name,
    String frequency,
    Integer targetDays,
    boolean archived,
    Instant createdAt
) {}

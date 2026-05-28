package com.example.habittracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHabitRequest(
    @NotBlank String name,
    @NotBlank String frequency,
    @NotNull @Min(1) @Max(7) Integer targetDays
) {}

package com.example.habittracker.progress.dto;

import java.util.List;

public record WeeklyProgressResponse(
    List<DayProgress> days,
    int currentStreak,
    int bestStreak
) {}

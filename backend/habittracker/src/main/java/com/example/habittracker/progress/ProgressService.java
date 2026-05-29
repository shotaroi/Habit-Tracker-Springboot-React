package com.example.habittracker.progress;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.habittracker.entity.Habit;
import com.example.habittracker.entity.HabitCompletion;
import com.example.habittracker.progress.dto.DayProgress;
import com.example.habittracker.progress.dto.WeeklyProgressResponse;
import com.example.habittracker.repository.HabitCompletionRepository;
import com.example.habittracker.repository.HabitRepository;

@Service
public class ProgressService {
    
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository completionRepository;

    public ProgressService(
        HabitRepository habitRepository,
        HabitCompletionRepository completionRepository
    ) {
        this.habitRepository = habitRepository;
        this.completionRepository = completionRepository;
    }

    public WeeklyProgressResponse getWeeklyProgress(Long userId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        List<Habit> activeHabits = habitRepository.findByUserIdAndArchivedFalse(userId);
        int totalHabits = activeHabits.size();

        List<HabitCompletion> completions = completionRepository.findByHabitUserIdAndDateBetween(userId, startDate, endDate);

        Map<LocalDate, Set<Long>> completedHabitIdsByDate = new HashMap<>();
        for (HabitCompletion completion : completions) {
            completedHabitIdsByDate
            .computeIfAbsent(completion.getDate(), d -> new HashSet<>())
            .add(completion.getHabit().getId());
        }

        List<DayProgress> days = new ArrayList<>();
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            int completedCount = completedHabitIdsByDate.getOrDefault(d, Collections.emptySet()).size();
            days.add(new DayProgress(d, completedCount, totalHabits));
        }

        Set<LocalDate> successDays = days.stream()
        .filter(day -> day.totalHabits() > 0 && day.completedHabits() >= day.totalHabits())
        .map(DayProgress::date)
        .collect(Collectors.toSet());

        int currentStreak = calculateCurrentStreak(successDays, LocalDate.now());
        int bestStreak = calculateBestStreak(successDays, startDate, endDate);

        return new WeeklyProgressResponse(days, currentStreak, bestStreak);
    }

    private int calculateCurrentStreak(Set<LocalDate> successDays, LocalDate today) {
        int streak = 0;
        LocalDate d = today;
        while (successDays.contains(d)) {
            streak++;
            d = d.minusDays(1);
        }
        return streak;
    }

    private int calculateBestStreak(Set<LocalDate> successDays, LocalDate start, LocalDate end) {
        int best = 0;
        int current = 0;

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            if (successDays. contains(d)) {
                current++;
                best = Math.max(best, current);
            } else {
                current = 0;
            }
        }
        return best;
    }
}

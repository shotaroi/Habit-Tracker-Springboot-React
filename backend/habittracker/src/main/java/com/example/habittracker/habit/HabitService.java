package com.example.habittracker.habit;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.habittracker.entity.Habit;
import com.example.habittracker.entity.HabitCompletion;
import com.example.habittracker.entity.User;
import com.example.habittracker.habit.dto.CreateHabitRequest;
import com.example.habittracker.habit.dto.HabitResponse;
import com.example.habittracker.repository.HabitCompletionRepository;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.UserRepository;

@Service
public class HabitService {
    
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository completionRepository;
    private final UserRepository userRepository;

    public HabitService(HabitRepository habitRepository,
                        HabitCompletionRepository completionRepository,
                        UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.completionRepository = completionRepository;
        this.userRepository = userRepository;
    }

    public HabitResponse create(Long userId, CreateHabitRequest req) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Habit h = new Habit();
        h.setUser(user);
        h.setName(req.name().trim());
        h.setFrequency(req.frequency().trim().toUpperCase());
        h.setTargetDays(req.targetDays());
        h.setArchived(false);

        return toResponse(habitRepository.save(h));
    }

    public List<HabitResponse> listActive(Long userId) {
        return habitRepository.findByUserIdAndArchivedFalse(userId)
        .stream()
        .map(this::toResponse)
        .toList();
    }

    public void complete(Long userId, Long habitId, LocalDate date) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));

            if (completionRepository.existsByHabitIdAndDate(habit.getId(), date)) return;

            HabitCompletion c = new HabitCompletion();
            c.setHabit(habit);
            c.setDate(date);
            completionRepository.save(c);
    }

    public List<Long> completedHabits(Long userId, LocalDate date) {
        return completionRepository.findByHabitUserIdAndDate(userId, date)
        .stream()
        .map(c -> c.getHabit().getId())
        .toList();
    }

    private HabitResponse toResponse(Habit h) {
        return new HabitResponse(h.getId(), h.getName(), h.getFrequency(), h.getTargetDays(), h.isArchived(), h.getCreatedAt());
    }

    public void archive(Long userId, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));

        habit.setArchived(true);
        habitRepository.save(habit);
    }
}

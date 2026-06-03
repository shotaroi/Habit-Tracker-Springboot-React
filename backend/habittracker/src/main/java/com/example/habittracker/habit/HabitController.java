package com.example.habittracker.habit;

import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.habit.dto.CreateHabitRequest;
import com.example.habittracker.habit.dto.HabitResponse;
import com.example.habittracker.security.AuthUserPrincipal;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/api/habits")
public class HabitController {
    
    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitResponse create(@Valid @RequestBody CreateHabitRequest request, @AuthenticationPrincipal AuthUserPrincipal user) {
        return habitService.create(user.userId(), request);
    }

    @GetMapping
    public List<HabitResponse> list(@AuthenticationPrincipal AuthUserPrincipal user) {
        return habitService.listActive(user.userId());
    }

    @GetMapping("/completed")
    public List<Long> completed(
        @RequestParam(required = false) 
        LocalDate date, @AuthenticationPrincipal
        AuthUserPrincipal user) {
            LocalDate targetDate = (date == null) ? LocalDate.now() : date;
            return habitService.completedHabits(user.userId(), targetDate);
    } 

    @PostMapping("/{habitId}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void complete(@PathVariable Long habitId, @RequestParam(required = false) LocalDate date, @AuthenticationPrincipal AuthUserPrincipal user) {
        habitService.complete(user.userId(), habitId, date == null ? LocalDate.now() : date);
    }

    @PostMapping("/{habitId}/archive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void archive(
        @PathVariable Long habitId, 
        @AuthenticationPrincipal AuthUserPrincipal user) {
            habitService.archive(user.userId(), habitId);
    }

    @GetMapping("/archived")
    public List<HabitResponse> listArchived(@AuthenticationPrincipal AuthUserPrincipal user) {
        return habitService.listArchived(user.userId());
    }

    @PostMapping("/{habitId}/unarchive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unarchive(
        @PathVariable Long habitId,
        @AuthenticationPrincipal AuthUserPrincipal user) {
            habitService.unarchive(user.userId(), habitId);
        }
}

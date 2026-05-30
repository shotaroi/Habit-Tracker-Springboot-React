package com.example.habittracker.habit;

import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entity.User;
import com.example.habittracker.habit.dto.CreateHabitRequest;
import com.example.habittracker.habit.dto.HabitResponse;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.security.AuthUserPrincipal;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.security.Principal;
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

    @PostMapping("/{habitId}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void complete(@PathVariable Long habitId, @RequestParam(required = false) LocalDate date, @AuthenticationPrincipal AuthUserPrincipal user) {
        habitService.complete(user.userId(), habitId, date == null ? LocalDate.now() : date);
    }
    
}

package com.example.habittracker.habit;

import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entity.User;
import com.example.habittracker.habit.dto.CreateHabitRequest;
import com.example.habittracker.habit.dto.HabitResponse;
import com.example.habittracker.repository.UserRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

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
    private final UserRepository userRepository;

    public HabitController(HabitService habitService, UserRepository userRepository) {
        this.habitService = habitService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitResponse create(@Valid @RequestBody CreateHabitRequest request, Principal principal) {
        Long userId = extractUserId(principal);
        return habitService.create(userId, request);
    }

    @GetMapping
    public List<HabitResponse> list(Principal principal) {
        Long userId = extractUserId(principal);
        return habitService.listActive(userId);
    }

    @PostMapping("/{habitId}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void complete(@PathVariable Long habitId,
                         @RequestParam(required = false) LocalDate date,
                         Principal principal) {
        Long userId = extractUserId(principal);
        habitService.complete(userId, habitId, date == null ? LocalDate.now() : date);
    }

    private Long extractUserId(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalStateException("Unauthenticated request");
        }

        User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        return user.getId();
    }
    
}

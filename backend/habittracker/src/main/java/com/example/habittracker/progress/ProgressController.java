package com.example.habittracker.progress;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entity.User;
import com.example.habittracker.progress.dto.WeeklyProgressResponse;
import com.example.habittracker.repository.UserRepository;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    
    private final ProgressService progressService;
    private final UserRepository userRepository;

    public ProgressController(ProgressService progressService, UserRepository userRepository) {
        this.progressService = progressService;
        this.userRepository = userRepository;
    }

    @GetMapping("/weekly")
    public WeeklyProgressResponse weekly(
        @RequestParam(required = false) LocalDate startDate,
        Principal principal
    ) {
        LocalDate start = (startDate == null) ? LocalDate.now().minusDays(6) : startDate;
        Long userId = extractUserId(principal);
        return progressService.getWeeklyProgress(userId, start);
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

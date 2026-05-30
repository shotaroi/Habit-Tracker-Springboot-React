package com.example.habittracker.progress;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entity.User;
import com.example.habittracker.progress.dto.WeeklyProgressResponse;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.security.AuthUserPrincipal;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    
    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/weekly")
    public WeeklyProgressResponse weekly(
        @RequestParam(required = false) LocalDate startDate, @AuthenticationPrincipal AuthUserPrincipal user) {
        LocalDate start = (startDate == null) ? LocalDate.now().minusDays(6) : startDate;
        return progressService.getWeeklyProgress(user.userId(), start);
    }
}

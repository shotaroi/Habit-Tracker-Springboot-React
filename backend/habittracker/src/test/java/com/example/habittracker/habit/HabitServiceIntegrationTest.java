package com.example.habittracker.habit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.habittracker.entity.User;
import com.example.habittracker.habit.dto.CreateHabitRequest;
import com.example.habittracker.habit.dto.HabitResponse;
import com.example.habittracker.repository.HabitCompletionRepository;
import com.example.habittracker.repository.UserRepository;

@SpringBootTest
@Transactional
public class HabitServiceIntegrationTest {
    
    @Autowired
    private HabitService habitService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitCompletionRepository completionRepository;

    @Test
    void complete_isIdempotent_forSameDate() {
        User user = createUser();
        HabitResponse habit = habitService.create(
            user.getId(),
            new CreateHabitRequest("Drink water", "DAILY", 7)
        );

        LocalDate today = LocalDate.now();

        habitService.complete(user.getId(), habit.id(), today);
        habitService.complete(user.getId(), habit.id(), today);

        assertTrue(completionRepository.existsByHabitIdAndDate(habit.id(), today));
        assertEquals(
            1,
            completionRepository.findByHabitUserIdAndDate(user.getId(), today).size(),
            "Expected only one completion for same habit/date"
        );
    }

    @Test
    void archive_and_unarchive_flow_works() {
        User user = createUser();
        HabitResponse habit = habitService.create(
            user.getId(),
            new CreateHabitRequest("Read book", "DAILY", 7) 
        );

        habitService.archive(user.getId(), habit.id());

        assertEquals(0, habitService.listActive(user.getId()).size());
        assertEquals(1, habitService.listArchived(user.getId()).size());
    
        habitService.unarchive(user.getId(), habit.id());

        assertEquals(1, habitService.listActive(user.getId()).size());
        assertEquals(0, habitService.listArchived(user.getId()).size());
    }

    @Test
    void cannot_archive_or_unarchive_other_users_habit() {
        User owner = createUser();
        User intruder = createUser();

        HabitResponse habit = habitService.create(
            owner.getId(), 
            new CreateHabitRequest("Workout", "DAILY", 7)
        );

        ResponseStatusException archiveException = assertThrows(
            ResponseStatusException.class,
            () -> habitService.archive(intruder.getId(), habit.id())
        );
        assertEquals(404, archiveException.getStatusCode().value());

        ResponseStatusException unarchiveException = assertThrows(
            ResponseStatusException.class,
            () -> habitService.unarchive(intruder.getId(), habit.id())
        );
        assertEquals(404, unarchiveException.getStatusCode().value());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test-" + UUID.randomUUID() + "@example.com");
        user.setPasswordHash("dummy-hash");
        return userRepository.save(user);
    }
}

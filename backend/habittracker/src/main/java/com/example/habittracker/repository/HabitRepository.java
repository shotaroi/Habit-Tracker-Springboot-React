package com.example.habittracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habittracker.entity.Habit;

public interface HabitRepository extends JpaRepository<Habit, Long>{
    List<Habit> findByUserIdAndArchivedFalse(Long userId);
    Optional<Habit> findByIdAndUserId(Long id, Long userId);

    List<Habit> findByUserIdAndArchivedTrue(Long userId);
}

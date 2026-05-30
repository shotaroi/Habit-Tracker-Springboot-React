package com.example.habittracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habittracker.entity.HabitCompletion;

public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long>{
    
    Optional<HabitCompletion> findByHabitIdAndDate(Long habitId, LocalDate date);

    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitCompletion> findByHabitIdAndDateBetween(Long habitId, LocalDate startDate, LocalDate endDate);

    List<HabitCompletion> findByHabitUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

}
